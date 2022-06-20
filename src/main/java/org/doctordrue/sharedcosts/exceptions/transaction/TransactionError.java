package org.doctordrue.sharedcosts.exceptions.transaction;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/21/2022
 **/
public enum TransactionError implements IErrorMessage {
   NOT_FOUND_BY_ID("TRNS-001", "Transaction id={transactionId} is no found"),
   NOT_FOUND_BY_TEXT(null, "Не могу найти такую транзакцию");

   TransactionError(String code, String template) {
      this.code = code;
      this.template = template;
   }

   private final String code;
   private final String template;

   @Override
   public String getCode() {
      return code;
   }

   @Override
   public String getTemplate() {
      return template;
   }
}
