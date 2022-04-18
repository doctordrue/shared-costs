package org.doctordrue.sharedcosts.exceptions.people;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public abstract class BasePersonServiceException extends BaseException {

   public BasePersonServiceException(PeopleError error) {
      super(error);
   }

   public BasePersonServiceException(PeopleError error, Throwable cause) {
      super(error, cause);
   }
}
