package org.doctordrue.sharedcosts.exceptions.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Person already exists")
public class PersonAlreadyExistsException extends BasePersonServiceException {

   private static final String MESSAGE = "Person already exists";

   public PersonAlreadyExistsException(String username) {
      super(PeopleError.ALREADY_EXISTS);
      setParameter("username", username);
   }
}
