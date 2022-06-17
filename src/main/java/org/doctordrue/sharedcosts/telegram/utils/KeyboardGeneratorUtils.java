package org.doctordrue.sharedcosts.telegram.utils;

import java.util.Collection;
import java.util.Collections;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
public class KeyboardGeneratorUtils {

   private static final String TRANSACTION_ITEM_TEMPLATE = "[%s] %s%s %s -> %s";

   public static ReplyKeyboardRemove removeKeyboard() {
      return ReplyKeyboardRemove.builder().removeKeyboard(true).build();
   }

   public static ReplyKeyboardMarkup selectGroupsKeyboard(Collection<Group> groups) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Group group : groups) {
         KeyboardButton button = KeyboardButton.builder().text(group.getName()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectCostKeyboard(Collection<Cost> costs) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Cost cost : costs) {
         KeyboardButton button = KeyboardButton.builder().text(cost.getName()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectTransactionKeyboard(Collection<Transaction> transactions) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Transaction transaction : transactions) {
         final String item = String.format(TRANSACTION_ITEM_TEMPLATE,
                 transaction.getId(),
                 transaction.getAmount(),
                 transaction.getCurrency().getShortName(),
                 transaction.getFrom().getFullName(),
                 transaction.getTo().getFullName());
         KeyboardButton button = KeyboardButton.builder().text(item).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectPaymentKeyboard(Collection<Payment> payments) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Payment payment : payments) {
         KeyboardButton button = KeyboardButton.builder().text(payment.getName()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectParticipationKeyboard(Collection<Participation> participations) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Participation participation : participations) {
         KeyboardButton button = KeyboardButton.builder().text(participation.getName()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectCurrencyKeyboard(Collection<Currency> currencies) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Currency currency : currencies) {
         KeyboardButton button = KeyboardButton.builder().text(currency.getShortName()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static ReplyKeyboardMarkup selectPersonKeyboard(Collection<Person> people) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (Person person : people) {
         KeyboardButton button = KeyboardButton.builder().text(person.getUsername()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   public static <T extends Enum<? extends KeyboardOption<? extends IBotState>>> ReplyKeyboardMarkup generateStaticKeyboard(Class<T> clazz) {
      ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = getBuilder();
      for (T option : clazz.getEnumConstants()) {
         KeyboardButton button = KeyboardButton.builder().text(((KeyboardOption<?>) option).getOption()).build();
         KeyboardRow row = new KeyboardRow();
         row.add(button);
         keyboardBuilder.keyboardRow(row);
      }
      return addStopButton(keyboardBuilder).build();
   }

   private static ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder addStopButton(ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder builder) {
      builder.keyboardRow(new KeyboardRow(Collections.singletonList(KeyboardButton.builder().text("/stop").build())));
      return builder;
   }

   private static ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder getBuilder() {
      return ReplyKeyboardMarkup.builder().resizeKeyboard(true);
   }

}
