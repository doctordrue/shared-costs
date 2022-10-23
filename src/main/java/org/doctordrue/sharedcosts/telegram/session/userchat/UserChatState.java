package org.doctordrue.sharedcosts.telegram.session.userchat;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.*;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.StateReactionFunction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.function.Function;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
public enum UserChatState implements IBotState<UserChatSession> {
   BEFORE_START("Наберите /start для начала работы"),
   SELECTING_GROUP("Выберите группу", s -> KeyboardGeneratorUtils.selectGroupsKeyboard(s.getAvailableGroups())),
   WORKING_WITH_GROUP("Что хотите делать с выбранной группой?", GroupAction.class),

   @Deprecated
   NEW_COST_AWAITING_NAME("Где потратили?"),
   @Deprecated
   NEW_COST_SELECTING_CURRENCY("В какой валюте?", s -> KeyboardGeneratorUtils.selectCurrencyKeyboard(s.getAvailableCurrencies())),

   SELECTING_COST("Выберите чек", s -> KeyboardGeneratorUtils.selectCostKeyboard(s.getSelectedGroup().getCosts())),
   NEW_TRANSACTION_SELECTING_CURRENCY("В какой валюте?", s -> KeyboardGeneratorUtils.selectCurrencyKeyboard(s.getAvailableCurrencies())),
   NEW_TRANSACTION_AWAITING_AMOUNT("Сколько получили?"),
   NEW_TRANSACTION_SELECTING_FROM("От кого получили?", s -> KeyboardGeneratorUtils.selectPersonKeyboard(s.getSelectedGroup().getParticipants())),
   SELECTING_TRANSACTION("Выберите перевод для редактирования", s -> KeyboardGeneratorUtils.selectTransactionKeyboard(s.getSelectedGroup().getTransactions())),
   WORKING_WITH_TRANSACTION("Что хотите сделать с выбранным переводом?", TransactionAction.class),

   WORKING_WITH_COST("Что хотите сделать с выбранным чеком?", CostAction.class),

   // Process new receipt
   PROCESS_COST_AWAITING_NAME("Где потратили?"),
   PROCESS_COST_SELECTING_CURRENCY("В какой валюте?", s -> KeyboardGeneratorUtils.selectCurrencyKeyboard(s.getAvailableCurrencies())),
   PROCESS_COST_SELECTING_ACTION("Что хотите сделать?", ProcessCostAction.class), //добавить в чек или завершить
   PROCESS_COST_SELECTING_PAYER("Кто платил?", s -> KeyboardGeneratorUtils.selectPersonKeyboard(s.getSelectedGroup().getParticipants())),
   PROCESS_COST_AWAITING_ITEM_NAME("Введите наименование"),
   PROCESS_COST_AWAITING_ITEM_PRICE("Введите цену"),

   // Allocate receipt items
   ALLOCATE_ITEMS_SELECTING_ITEM("Выберите наименование", s -> KeyboardGeneratorUtils.selectParticipationKeyboard(s.getSelectedCost().getParticipations())),
   ALLOCATE_ITEMS_SELECTING_PARTICIPANT("Кто заказывал?", s -> KeyboardGeneratorUtils.selectPersonKeyboard(s.getSelectedParticipation().getPotentialParticipants())),
   ALLOCATE_ITEMS_SELECTING_ACTION("Кто-то еще?", ItemParticipantsAction.class),

   @Deprecated
   SELECTING_PAYMENT("Выберите оплату", s -> KeyboardGeneratorUtils.selectPaymentKeyboard(s.getSelectedCost().getPayments())),
   @Deprecated
   NEW_PAYMENT_AWAITING_AMOUNT("Сколько заплатили?"),
   NEW_PAYMENT_SELECTING_WHO("Кто платил?", s -> KeyboardGeneratorUtils.selectPersonKeyboard(s.getSelectedGroup().getParticipants())),
   WORKING_WITH_PAYMENT("Что хотите сделать с выбранной оплатой?", PaymentAction.class),
   @Deprecated
   SELECTING_PARTICIPATION("Какую позицию хотите отредактировать?", s -> KeyboardGeneratorUtils.selectParticipationKeyboard(s.getSelectedCost().getParticipations())),
   @Deprecated
   NEW_PARTICIPATION_AWAITING_NAME("Какое наименование?"),
   NEW_PARTICIPATION_AWAITING_PRICE("Сколько стоит?"),
   NEW_PARTICIPATION_AWAITING_WHO("Кто это заказывал?", s -> KeyboardGeneratorUtils.selectPersonKeyboard(s.getSelectedGroup().getParticipants())),
   WORKING_WITH_PARTICIPATION("Что хотите сделать с выбранной позицией?", ParticipationAction.class);

   private final String message;
   private final StateReactionFunction<UserChatSession> onStateReaction;

   UserChatState(String message) {
      this.message = message;
      this.onStateReaction = s -> SendMessage.builder()
              .chatId(s.getChatId().toString())
              .text(this.getMessage())
              .replyMarkup(KeyboardGeneratorUtils.removeKeyboard())
              .build();
   }

   UserChatState(String message, Function<UserChatSession, ReplyKeyboard> keyboardFunction) {
      this.message = message;
      this.onStateReaction = s -> SendMessage.builder()
              .chatId(s.getChatId().toString())
              .text(this.getMessage())
              .replyMarkup(keyboardFunction.apply(s))
              .build();
   }

   UserChatState(String message, Class<? extends Enum<? extends KeyboardOption<UserChatState>>> onStateKeyboard) {
      this.message = message;
      this.onStateReaction = s -> SendMessage.builder()
              .chatId(s.getChatId().toString())
              .text(this.getMessage())
              .replyMarkup(KeyboardGeneratorUtils.generateStaticKeyboard(onStateKeyboard))
              .build();
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public StateReactionFunction<UserChatSession> getOnStateReaction() {
      return onStateReaction;
   }
}
