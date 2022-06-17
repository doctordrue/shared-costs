package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public enum UpdateProcessorError implements IErrorMessage {
   MESSAGE_NOT_SUPPORTED(null, "Данный тип сообщений не поддерживается."),
   SOMETHING_WENT_WRONG(null, "Что-то пошло не так.");

   private final String code;
   private final String template;

   UpdateProcessorError(String code, String template) {
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
