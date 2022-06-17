package org.doctordrue.telegram.bot.api.keyboards.exceptions;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public enum KeyboardErrorMessage implements IErrorMessage {
   NOT_FOUND("", "Не знаю что ответить на ваше '${message}'.");

   private final String code;
   private final String template;

   KeyboardErrorMessage(String code, String template) {
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
