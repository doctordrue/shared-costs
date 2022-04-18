package org.doctordrue.sharedcosts.exceptions.people;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class PersonNotFoundException extends BasePersonServiceException {

   public PersonNotFoundException(Long id) {
      super(PeopleError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   @Override
   public String getMessage() {
      return super.getMessage();
   }
}
