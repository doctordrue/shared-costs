package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

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

   public ProcessCostActionProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, ProcessCostAction.class);
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {

   }
}
