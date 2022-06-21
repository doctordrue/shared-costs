package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.ParticipationProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.exceptions.group.ParticipantNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
@Component
public class SelectingNewParticipationWhoProcessor extends BaseStaticKeyboardReplyUserChatProcessor<CostAction> {

   private final ParticipationProcessingService participationProcessingService;
   private final PersonService personService;

   public SelectingNewParticipationWhoProcessor(UserChatSessionWorker sessionWorker, ParticipationProcessingService participationProcessingService, PersonService personService) {
      super(sessionWorker, UserChatState.WORKING_WITH_COST, CostAction.class);
      this.participationProcessingService = participationProcessingService;
      this.personService = personService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Group group = this.getSession(update).getSelectedGroup();
         if (group.isParticipated(text)) {
            UserChatSession session = this.getSession(update);
            String name = session.getTempParticipationName();
            Double price = session.getTempParticipationAmount();
            Person person = this.personService.findByUsername(text);
            Cost cost = session.getSelectedCost();

            Participation participation = new Participation()
                    .setName(name)
                    .setAmount(price)
                    .addPerson(person)
                    .setCost(cost);
            this.participationProcessingService.processNew(participation, false);
            this.updateSession(update, s -> s.setTempParticipationAmount(null)
                    .setTempParticipationName(null));
            return true;
         }
         throw new ParticipantNotFoundException(text, group);
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
