package org.doctordrue.sharedcosts.exceptions.payment;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class BasePaymentServiceException extends BaseException {

   public BasePaymentServiceException(PaymentError error) {
      super(error);
   }

   public BasePaymentServiceException(PaymentError error, Throwable cause) {
      super(error, cause);
   }
}
