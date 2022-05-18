package org.doctordrue.sharedcosts.telegram.data.entities;

/**
 * @author Andrey_Barantsev
 * 5/13/2022
 **/
public enum BotStatus {
   BEFORE_START("не инициализирован"), // Actions:
   // init -> IDLE

   IDLE("ожидаю команду"), // Actions:
   // add cost -> AWAITING_NEW_COST_NAME,
   // select existing cost -> SELECTING_COST,
   // add transaction -> AWAITING_TRANSACTION_AMOUNT
   // show debts -> IDLE

   // Add cost process
   AWAITING_NEW_COST_NAME("жду ввода названия расходов"), // Actions:
   // correct name entered -> AWAITING_NEW_COST_AMOUNT
   // error -> AWAITING_NEW_COST_NAME
   // cancel -> IDLE
   AWAITING_NEW_COST_AMOUNT("жду ввода количества потраченных денег"), // Actions:
   // correct amount entered -> COST_SELECTED
   // error -> AWAITING_NEW_COST_AMOUNT
   // cancel -> IDLE

   // Select existing cost process
   SELECTING_COST("жду выбора с каким из существующих расходов работать"), // Actions:
   // select cost from list -> COST_SELECTED
   // load more -> load more costs & SELECTING_COST
   // cancel -> IDLE

   // Working with cost process
   COST_SELECTED("ожидаю действия с выбранным расходом"), // Actions:
   // edit -> COST_EDITING
   // add payment -> AWAITING_PAYMENT_AMOUNT
   // add participation -> AWAITING_PARTICIPATION_NAME
   // delete payment ->
   // delete participation ->
   // back -> IDLE

   // Cost editing process
   COST_EDITING("жду ответа что хотим отредактировать в выбранном расходе"), // Actions:
   // edit name -> AWAITING_COST_NAME
   // edit amount -> AWAITING_COST_AMOUNT
   // delete : success -> IDLE
   //          fail    -> COST_EDITING
   // back -> COST_SELECTED
   AWAITING_COST_NAME("жду ввода нового названия расходов"), // Actions:
   // correct name entered -> COST_EDITING
   // error -> AWAITING_COST_NAME
   // cancel -> COST_EDITING
   AWAITING_COST_AMOUNT("жду ввода обновленного количества потраченных денег"), // Actions:
   // correct amount entered -> COST_EDITING
   // error -> AWAITING_COST_AMOUNT
   // cancel -> COST_EDITING

   // Payment person field automatically set to current user
   AWAITING_PAYMENT_AMOUNT("жду ввода количества оплаченных средств"), // Actions:
   // correct amount entered -> AWAITING_PAYMENT_NAME
   // error -> AWAITING_PAYMENT_AMOUNT
   // cancel -> COST_SELECTED
   AWAITING_PAYMENT_NAME("жду ввода названия оплаты"), // Actions:
   // skip -> COST_SELECTED
   // correct name entered -> save & COST_SELECTED
   // error -> AWAITING_PAYMENT_NAME
   // cancel -> COST_SELECTED

   AWAITING_PARTICIPATION_NAME("жду ввода названия позиции"), // Actions:
   // correct name entered -> AWAITING_PARTICIPATION_AMOUNT
   // error -> AWAITING_PARTICIPATION_NAME
   // cancel -> COST_SELECTED
   AWAITING_PARTICIPATION_AMOUNT("жду ввода цены позиции"),
   // correct amount entered -> AWAITING_PARTICIPANTS
   // error -> AWAITING_PARTICIPATION_AMOUNT,
   // cancel -> COST_SELECTED
   AWAITING_PARTICIPANTS("жду добавления потребителя позиции"), // Actions:
   // me -> AWAITING_PARTICIPANTS
   // group member selected -> AWAITING_PARTICIPANTS
   // done -> save & COST_SELECTED

   // Transaction from automatically set to current user
   AWAITING_TRANSACTION_AMOUNT("жду ввода суммы переданных денег"),
   AWAITING_TRANSACTION_TO("жду выбора получателя денег");

   private final String message;

   BotStatus(String message) {
      this.message = message;
   }

   public String getMessage() {
      return message;
   }
}
