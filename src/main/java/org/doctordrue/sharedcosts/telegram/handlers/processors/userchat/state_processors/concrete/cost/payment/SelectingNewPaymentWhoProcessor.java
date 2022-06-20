package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment;

import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.PaymentsProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class SelectingNewPaymentWhoProcessor extends BaseStaticKeyboardReplyUserChatProcessor<CostAction> {
   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final PersonService personService;
   private final PaymentsProcessingService paymentService;

   public SelectingNewPaymentWhoProcessor(UserChatSessionWorker sessionWorker, PersonService personService, PaymentsProcessingService paymentService) {
      super(sessionWorker, TARGET_STATE, CostAction.class);
      this.personService = personService;
      this.paymentService = paymentService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Optional<Person> maybePerson = this.personService.find(text);
         if (maybePerson.isPresent() && this.getSession(update).getSelectedGroup().isParticipated(maybePerson.get().getUsername())) {
            //ready to persist payment
            UserChatSession session = this.getSession(update);
            String name = session.getSelectedCost().getName() + " оплата";
            Double amount = session.getTempPaymentAmount();
            Person person = maybePerson.get();
            Cost cost = session.getSelectedCost();

            Payment payment = new Payment().setName(name)
                    .setAmount(amount)
                    .setPerson(person)
                    .setCost(cost);

            this.paymentService.processNew(payment, true);
            this.updateSession(update, s -> s.setTempPaymentAmount(null));
            return true;
         }
      }
      return false;
   }

   @Override
   protected String getExpectedInputMessage(UserChatSession session) {
      return String.format("Оплата для '%s' создана. Что будем делать дальше?", session.getSelectedCost().getName());
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
