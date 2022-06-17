package org.doctordrue.sharedcosts.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
public abstract class BaseException extends RuntimeException {

   private final String code;
   private final String template;
   private final Map<String, Object> parameters = new HashMap<>();

   public BaseException(IErrorMessage errorMessage) {
      this.code = errorMessage.getCode();
      this.template = errorMessage.getTemplate();
   }

   public BaseException(IErrorMessage errorMessage, Throwable cause) {
      super(cause);
      this.code = errorMessage.getCode();
      this.template = errorMessage.getTemplate();
   }

   public void setParameter(String name, Object value) {
      parameters.put(name, value);
   }

   @Override
   public String getMessage() {
      StringSubstitutor sub = new StringSubstitutor(this.parameters);
      String message = StringUtils.isNotBlank(this.code) ? this.code + ": " + sub.replace(this.template) : sub.replace(this.template);
      if (this.getCause() != null) {
         message += " caused by " + this.getCause().toString();
      }
      return message;
   }
}
