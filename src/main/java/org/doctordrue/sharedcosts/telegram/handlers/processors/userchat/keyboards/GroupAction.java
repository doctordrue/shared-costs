package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public enum GroupAction implements KeyboardOption<UserChatState> {
   CREATE_COST("Добавить новый чек", UserChatState.NEW_COST_AWAITING_NAME),
   EDIT_COST("Редактировать чек", UserChatState.SELECTING_COST),
   ADD_TRANSACTION("Создать транзакцию", UserChatState.NEW_TRANSACTION_SELECTING_CURRENCY),
   EDIT_TRANSACTION("Редактировать транзакцию", UserChatState.SELECTING_TRANSACTION);

   GroupAction(String option) {
      this(option, null);
   }

   GroupAction(String option, UserChatState targetState) {
      this.option = option;
      this.targetState = targetState;
   }

   private final String option;
   private final UserChatState targetState;

   @Override
   public String getOption() {
      return this.option;
   }

   @Override
   public UserChatState getTargetState() {
      return this.targetState;
   }
}
