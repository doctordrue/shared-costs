package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

import java.time.LocalDateTime;
import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
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
 * 6/24/2022
 **/
@Component
public class ProcessCostSelectingPayerProcessor extends BaseSingleStateUserChatProcessor {

   private final PersonService personService;
   private final CostProcessingService costProcessingService;

   public ProcessCostSelectingPayerProcessor(UserChatSessionWorker sessionWorker, PersonService personService, CostProcessingService costProcessingService) {
      super(sessionWorker, UserChatState.PROCESS_COST_SELECTING_ACTION);
      this.personService = personService;
      this.costProcessingService = costProcessingService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Создаем чек *'%s'*.\n Оплатил: %s",
              session.getSelectedCost().getName(),
              session.getTempCostPayer().toTelegramString());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Optional<Person> maybePerson = this.personService.find(text);
      if (maybePerson.isPresent() && session.getSelectedGroup().isParticipated(maybePerson.get().getUsername())) {
         LocalDateTime now = LocalDateTime.now();
         // create cost
         Cost cost = this.costProcessingService.addCost(session.getTempCostName(), session.getSelectedGroup(), session.getCurrency(), 0.0, now);
         this.updateSession(session, s -> s.setSelectedCost(cost).setTempCostPayer(maybePerson.get()));
      } else {
         throw new ParticipantNotFoundException(text, session.getSelectedGroup());
      }
   }
}
