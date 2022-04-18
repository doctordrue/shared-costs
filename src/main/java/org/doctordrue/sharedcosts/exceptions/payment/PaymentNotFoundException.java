package org.doctordrue.sharedcosts.exceptions.payment;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class PaymentNotFoundException extends BasePaymentServiceException {

   public PaymentNotFoundException(Long id) {
      super(PaymentError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   public PaymentNotFoundException(Long id, Throwable cause) {
      super(PaymentError.NOT_FOUND_BY_ID, cause);
      setParameter("id", id);
   }
}
