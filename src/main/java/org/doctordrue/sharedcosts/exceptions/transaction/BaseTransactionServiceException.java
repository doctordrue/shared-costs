package org.doctordrue.sharedcosts.exceptions.transaction;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/21/2022
 **/
public class BaseTransactionServiceException extends BaseException {

   public BaseTransactionServiceException(TransactionError error) {
      super(error);
   }
}
