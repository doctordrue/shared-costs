package org.doctordrue.sharedcosts.exceptions;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class PaymentsException extends BaseException{

   private Double costs;
   private Double payments;

   public PaymentsException(Double costs, Double payments) {
      super("SC100");
      this.costs = costs;
      this.payments = payments;
   }

   @Override
   public String getMessage() {
      String message = "";
      if (this.costs > this.payments) {
         message = String.format("Payments are less then cost, please add new payment record for cost or edit existing: %s < %s", payments, costs);
      } else {
         message  = String.format("Payments are more then required, please edit existing payments for cost or remove one: %s > %s", payments, costs);
      }
      return message;
   }
}
