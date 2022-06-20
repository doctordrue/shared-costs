package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public enum GroupAction implements KeyboardOption<UserChatState> {
   CREATE_COST("Я хочу добавить новый чек", UserChatState.NEW_COST_AWAITING_NAME),
   EDIT_COST("Я хочу отредактировать добавленный чек", UserChatState.SELECTING_COST),
   ADD_TRANSACTION("Я получил деньги от участника группы", UserChatState.NEW_TRANSACTION_SELECTING_CURRENCY),
   EDIT_TRANSACTION("Я хочу отредактировать денежный перевод между участниками группы", UserChatState.SELECTING_TRANSACTION),
   SHOW_INFO("Я хочу посмотреть информацию", UserChatState.WORKING_WITH_GROUP);

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
