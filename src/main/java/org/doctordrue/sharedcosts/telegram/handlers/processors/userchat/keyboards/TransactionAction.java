package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/20/2022
 **/
public enum TransactionAction implements KeyboardOption<UserChatState> {
   REMOVE("Удалить", UserChatState.WORKING_WITH_GROUP);

   private final String option;
   private final UserChatState targetState;

   TransactionAction(String option, UserChatState targetState) {
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
