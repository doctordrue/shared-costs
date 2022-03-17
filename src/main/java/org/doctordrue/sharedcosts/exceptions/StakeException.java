package org.doctordrue.sharedcosts.exceptions;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class StakeException extends BaseException{


   private Double costs;
   private Double stakes;

   public StakeException(Double costs, Double stakes) {
      super("SC101");
      this.costs = costs;
      this.stakes = stakes;
   }

   @Override
   public String getMessage() {
      String message = "";
      if (this.costs > this.stakes) {
         message = String.format("Stakes are less then total cost, please add new stake record for cost or edit existing: %s < %s", stakes, costs);
      } else {
         message  = String.format("Stakes are more then required, please edit existing stake for cost or/and remove one: %s > %s", stakes, costs);
      }
      return message;
   }

}
