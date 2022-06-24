package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/21/2022
 **/
public enum PaymentAction implements KeyboardOption<UserChatState> {
   VIEW("Посмотреть информацию об оплате", UserChatState.WORKING_WITH_PAYMENT),
   REMOVE("Удалить оплату", UserChatState.WORKING_WITH_COST),
   RETURN("Назад к выбору оплаты", UserChatState.SELECTING_PAYMENT);

   private final String option;
   private final UserChatState targetState;

   PaymentAction(String option, UserChatState targetState) {
      this.option = option;
      this.targetState = targetState;
   }

   @Override
   public String getOption() {
      return this.option;
   }

   @Override
   public UserChatState getTargetState() {
      return this.targetState;
   }
}
