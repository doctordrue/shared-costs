package org.doctordrue.sharedcosts.exceptions.parse;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public enum ParseError implements IErrorMessage {
   MONEY_AMOUNT_PARSE_ERROR(null, "Некорректный формат суммы. Введите положительное число с десятичной точкой и максимум 2-мя цифрами после точки, например 220.55.");

   private final String code;
   private final String template;

   ParseError(String code, String template) {
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
