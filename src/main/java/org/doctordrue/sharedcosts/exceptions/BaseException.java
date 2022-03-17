package org.doctordrue.sharedcosts.exceptions;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
public class BaseException extends RuntimeException {

   private String code;

   public BaseException(String code){
      this.code = code;
   }

   public BaseException(String code, String message) {
      super(message);
      this.code = code;
   }

   public BaseException(String code, String message, Throwable cause) {
      super(message, cause);
      this.code = code;
   }

   @Override
   public String getMessage() {
      return code + ":" + super.getMessage();
   }
}
