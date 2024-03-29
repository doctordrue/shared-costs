package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment;

import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.PaymentsProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Payment;
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
 * 6/9/2022
 **/
@Component
public class SelectingNewPaymentWhoProcessor extends BaseSingleStateUserChatProcessor {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final PersonService personService;
   private final PaymentsProcessingService paymentService;

   public SelectingNewPaymentWhoProcessor(UserChatSessionWorker sessionWorker, PersonService personService, PaymentsProcessingService paymentService) {
      super(sessionWorker, TARGET_STATE);
      this.personService = personService;
      this.paymentService = paymentService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Оплата чека '%s' успешно создана";
      return String.format(template, session.getSelectedCost().getName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Optional<Person> maybePerson = this.personService.find(text);
      if (maybePerson.isPresent() && session.getSelectedGroup().isParticipated(maybePerson.get().getUsername())) {
         //ready to persist payment
         String name = session.getSelectedCost().getName() + " оплата";
         Double amount = session.getTempPaymentAmount();
         Person person = maybePerson.get();
         Cost cost = session.getSelectedCost();

         Payment payment = new Payment().setName(name)
                 .setAmount(amount)
                 .setPerson(person)
                 .setCost(cost);

         this.paymentService.processNew(payment, true);
         this.updateSession(session, s -> s.setTempPaymentAmount(null));
      } else {
         throw new ParticipantNotFoundException(text, session.getSelectedGroup());
      }
   }
}
