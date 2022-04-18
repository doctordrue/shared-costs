package org.doctordrue.sharedcosts.exceptions.currency;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class BaseCurrencyServiceException extends BaseException {

   public BaseCurrencyServiceException(CurrencyError error) {
      super(error);
   }

   public BaseCurrencyServiceException(CurrencyError error, Throwable cause) {
      super(error, cause);
   }
}
