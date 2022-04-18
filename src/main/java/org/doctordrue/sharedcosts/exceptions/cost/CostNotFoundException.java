package org.doctordrue.sharedcosts.exceptions.cost;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class CostNotFoundException extends BaseCostServiceException {

   public CostNotFoundException(Long id) {
      super(CostError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   public CostNotFoundException(Long id, Throwable cause) {
      super(CostError.NOT_FOUND_BY_ID, cause);
      setParameter("id", id);
   }
}
