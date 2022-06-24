package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.ParticipationProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.group.ParticipantNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
@Component
public class SelectingNewParticipationWhoProcessor extends BaseSingleStateUserChatProcessor {

   private final ParticipationProcessingService participationProcessingService;
   private final PersonService personService;

   public SelectingNewParticipationWhoProcessor(UserChatSessionWorker sessionWorker, ParticipationProcessingService participationProcessingService, PersonService personService) {
      super(sessionWorker, UserChatState.WORKING_WITH_COST);
      this.participationProcessingService = participationProcessingService;
      this.personService = personService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Позиция в чеке '%s' успешно создана";
      return String.format(template, session.getSelectedCost().getName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Group group = session.getSelectedGroup();
      if (group.isParticipated(text)) {
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
         this.updateSession(session, s -> s.setTempParticipationAmount(null)
                 .setTempParticipationName(null));
      } else {
         throw new ParticipantNotFoundException(text, group);
      }
   }
}
