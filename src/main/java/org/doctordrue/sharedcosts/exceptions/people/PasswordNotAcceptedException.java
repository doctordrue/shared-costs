package org.doctordrue.sharedcosts.exceptions.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordNotAcceptedException extends BasePasswordException {

   public PasswordNotAcceptedException() {
      super(PeopleError.PASSWORD_NOT_ACCEPTED);
   }
}
