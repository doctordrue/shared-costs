package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public enum GroupAction implements KeyboardOption<UserChatState> {
   CREATE_COST("Добавить новый чек", UserChatState.NEW_COST_AWAITING_NAME),
   PROCESS_COST("Ввести чек и позиции", UserChatState.PROCESS_COST_AWAITING_NAME),
   EDIT_COST("Отредактировать чек", UserChatState.SELECTING_COST),
   ADD_TRANSACTION("Я получил деньги от участника группы", UserChatState.NEW_TRANSACTION_SELECTING_CURRENCY),
   EDIT_TRANSACTION("Я хочу отредактировать денежный перевод между участниками группы", UserChatState.SELECTING_TRANSACTION),
   SHOW_INFO("Посмотреть баланс группы", UserChatState.WORKING_WITH_GROUP),
   RETURN("Назад к выбору группы", UserChatState.SELECTING_GROUP);

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
