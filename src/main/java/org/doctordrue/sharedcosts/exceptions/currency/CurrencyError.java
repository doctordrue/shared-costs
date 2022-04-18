package org.doctordrue.sharedcosts.exceptions.currency;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum CurrencyError implements IErrorMessage {
   NOT_FOUND_BY_ID("CRNC-001", "Currency not found for id=${id}"),
   NOT_FOUND_BY_NAME("CRNC-002", "Currency not found for short name = ${shortName}");

   private final String code;
   private final String template;

   CurrencyError(String code, String template) {
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
