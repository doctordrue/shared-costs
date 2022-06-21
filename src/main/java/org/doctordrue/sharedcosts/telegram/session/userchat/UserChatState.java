package org.doctordrue.sharedcosts.telegram.session.userchat;

import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.ParticipationAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.PaymentAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.TransactionAction;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
public enum UserChatState implements IBotState {
   BEFORE_START("Наберите /start для начала работы"),
   SELECTING_GROUP("Выберите группу"),

   WORKING_WITH_GROUP("Что хотите делать с выбранной группой?", GroupAction.class),
   NEW_COST_AWAITING_NAME("Где потратили?"),
   NEW_COST_SELECTING_CURRENCY("В какой валюте?"),
   SELECTING_COST("Выберите чек"),
   NEW_TRANSACTION_SELECTING_CURRENCY("В какой валюте?"),
   NEW_TRANSACTION_AWAITING_AMOUNT("Сколько получили?"),
   NEW_TRANSACTION_SELECTING_FROM("От кого получили?"),
   SELECTING_TRANSACTION("Выберите перевод для редактирования"),
   WORKING_WITH_TRANSACTION("Что хотите сделать с выбранным переводом?", TransactionAction.class),

   WORKING_WITH_COST("Что хотите сделать с выбранным чеком?", CostAction.class),

   SELECTING_PAYMENT("Выберите оплату"),
   NEW_PAYMENT_AWAITING_AMOUNT("Сколько заплатили?"),
   NEW_PAYMENT_SELECTING_WHO("Кто платил?"),
   WORKING_WITH_PAYMENT("Что хотите сделать с выбранной оплатой?", PaymentAction.class),

   SELECTING_PARTICIPATION("Какую позицию хотите отредактировать?"),
   NEW_PARTICIPATION_AWAITING_NAME("Какое наименование?"),
   NEW_PARTICIPATION_AWAITING_PRICE("Сколько стоит?"),
   NEW_PARTICIPATION_AWAITING_WHO("Кто это заказывал?"),
   WORKING_WITH_PARTICIPATION("Что хотите сделать с выбранной позицией?", ParticipationAction.class);

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
