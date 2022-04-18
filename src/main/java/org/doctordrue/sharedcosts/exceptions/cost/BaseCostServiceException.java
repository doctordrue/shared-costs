package org.doctordrue.sharedcosts.exceptions.cost;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class BaseCostServiceException extends BaseException {

   public BaseCostServiceException(CostError error) {
      super(error);
   }

   public BaseCostServiceException(CostError error, Throwable cause) {
      super(error, cause);
   }
}
