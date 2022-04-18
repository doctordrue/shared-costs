package org.doctordrue.sharedcosts.exceptions.currency;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class CurrencyNotFoundException extends BaseCurrencyServiceException {

   public CurrencyNotFoundException(Long id) {
      super(CurrencyError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   public CurrencyNotFoundException(Long id, Throwable cause) {
      super(CurrencyError.NOT_FOUND_BY_ID, cause);
      setParameter("id", id);
   }

   public CurrencyNotFoundException(String shortName) {
      super(CurrencyError.NOT_FOUND_BY_NAME);
      setParameter("shortName", shortName);
   }

   public CurrencyNotFoundException(String shortName, Throwable cause) {
      super(CurrencyError.NOT_FOUND_BY_NAME, cause);
      setParameter("shortName", shortName);
   }
}
