package org.doctordrue.sharedcosts.exceptions.people;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public abstract class BasePasswordException extends BasePersonServiceException {

   public BasePasswordException(PeopleError error) {
      super(error);
   }
}
