package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/23/2022
 **/
public enum ProcessCostAction implements KeyboardOption<UserChatState> {
   ADD_ITEM("Добавить позицию в чек", UserChatState.PROCESS_COST_AWAITING_ITEM_NAME),
   FINISH("Завершить формирование чека", UserChatState.WORKING_WITH_COST);
   private final String option;
   private final UserChatState targetState;

   ProcessCostAction(String option, UserChatState targetState) {
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
