package org.doctordrue.sharedcosts.exceptions.cost;

/**
 * @author Andrey_Barantsev
 * 6/24/2022
 **/
public class CostNameExistException extends BaseCostServiceException {

   public CostNameExistException(String name) {
      super(CostError.ALREADY_EXIST);
      setParameter("name", name);
   }
}
