package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
public enum CostAction implements KeyboardOption<UserChatState> {
    ALLOCATE_ITEMS("Распределить позиции в чеке", UserChatState.ALLOCATE_ITEMS_SELECTING_ITEM),
    VIEW("Показать информацию о чеке", UserChatState.WORKING_WITH_COST),
    REMOVE("Удалить чек", UserChatState.WORKING_WITH_GROUP),
    RETURN("Назад к выбору чека", UserChatState.SELECTING_COST),

//    @Deprecated
//   ADD_PAYMENT("Оплатить", UserChatState.NEW_PAYMENT_AWAITING_AMOUNT),
//    @Deprecated
//   EDIT_PAYMENT("Редактировать оплаченное", UserChatState.SELECTING_PAYMENT),
//    @Deprecated
//   ADD_PARTICIPATION("Поучаствовать", UserChatState.NEW_PARTICIPATION_AWAITING_NAME),
//    @Deprecated
//   EDIT_PARTICIPATION("Поменять существующее участие", UserChatState.SELECTING_PARTICIPATION)
    ;


    private final String option;
    private final UserChatState targetState;

    CostAction(String option, UserChatState targetState) {
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
