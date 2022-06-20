package org.doctordrue.sharedcosts.exceptions.transaction;

/**
 * @author Andrey_Barantsev
 * 4/21/2022
 **/
public class TransactionNotFoundException extends BaseTransactionServiceException {

   public TransactionNotFoundException(Long id) {
      super(TransactionError.NOT_FOUND_BY_ID);
      setParameter("transactionId", id);
   }

   public TransactionNotFoundException() {
      super(TransactionError.NOT_FOUND_BY_TEXT);
   }

}