package org.doctordrue.sharedcosts.exceptions.people;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordNotConfirmedException extends BasePasswordException {

   public PasswordNotConfirmedException() {
      super(PeopleError.PASSWORD_NOT_CONFIRMED);
   }
}
