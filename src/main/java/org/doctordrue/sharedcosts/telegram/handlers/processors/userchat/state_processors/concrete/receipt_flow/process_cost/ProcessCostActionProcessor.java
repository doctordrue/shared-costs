package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

import org.doctordrue.sharedcosts.business.services.processing.PaymentsProcessingService;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.ProcessCostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardAnswerUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/24/2022
 **/
@Component
public class ProcessCostActionProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<ProcessCostAction> {

   private final PaymentsProcessingService paymentsProcessingService;

   public ProcessCostActionProcessor(UserChatSessionWorker sessionWorker, PaymentsProcessingService paymentsProcessingService) {
      super(sessionWorker, ProcessCostAction.class);
      this.paymentsProcessingService = paymentsProcessingService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      UserChatSession session = this.getSession(update);
      if (newState == UserChatState.WORKING_WITH_COST) {
         // create payment
         this.paymentsProcessingService.processNew(new Payment()
                 .setCost(session.getSelectedCost())
                 .setAmount(session.getSelectedCost().getTotal())
                 .setPerson(session.getTempCostPayer())
                 .setName(session.getSelectedCost().getName()), false);
         this.updateSession(session, s -> s.setTempCostPayer(null));
      }

   }
}
