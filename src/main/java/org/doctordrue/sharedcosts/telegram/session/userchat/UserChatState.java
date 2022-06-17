package org.doctordrue.sharedcosts.telegram.session.userchat;

import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
public enum UserChatState implements IBotState {
   BEFORE_START("Работа не начата"),
   SELECTING_GROUP("Ожидание выбора группы совместных расходов"),

   WORKING_WITH_GROUP("Группа совместных расходов выбрана", GroupAction.class),
   NEW_COST_AWAITING_NAME("Где потратили?"),
   NEW_COST_SELECTING_CURRENCY("В какой валюте?"),
   SELECTING_COST("Ожидание выбора статьи расходов"),
   NEW_TRANSACTION_SELECTING_CURRENCY("В какой валюте?"),
   SELECTING_TRANSACTION("Выберите транзакцию для редактирования"),

   WORKING_WITH_COST("Что хотите сделать?", CostAction.class),

   SELECTING_PAYMENT("Какую оплату хотите отредактировать?"),
   NEW_PAYMENT_AWAITING_AMOUNT("Сколько заплатили?"),
   NEW_PAYMENT_SELECTING_WHO("Кто платил?"),

   SELECTING_PARTICIPATION("Какое наименование хотите отредактировать?"),
   NEW_PARTICIPATION_AWAITING_NAME("Какое наименование?"),
   NEW_PARTICIPATION_AWAITING_PRICE("Сколько стоит?"),
   NEW_PARTICIPATION_AWAITING_WHO("Кто это заказывал?");

   private final String message;
   private final Class<? extends Enum<? extends KeyboardOption<UserChatState>>> onStateKeyboard;

   UserChatState(String message) {
      this.message = message;
      this.onStateKeyboard = null;
   }

   UserChatState(String message, Class<? extends Enum<? extends KeyboardOption<UserChatState>>> onStateKeyboard) {
      this.message = message;
      this.onStateKeyboard = onStateKeyboard;
   }

   public String getMessage() {
      return this.message;
   }

   public Class<? extends Enum<? extends KeyboardOption<? extends IBotState>>> getOnStateKeyboard() {
      return this.onStateKeyboard;
   }
}
