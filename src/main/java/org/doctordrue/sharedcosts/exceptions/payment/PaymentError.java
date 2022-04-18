package org.doctordrue.sharedcosts.exceptions.payment;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum PaymentError implements IErrorMessage {
   NOT_FOUND_BY_ID("PMNT-0001", "Payment not found for id=${id}");

   private final String code;
   private final String template;

   PaymentError(String code, String template) {
      this.code = code;
      this.template = template;
   }

   @Override
   public String getCode() {
      return code;
   }

   @Override
   public String getTemplate() {
      return template;
   }
}
