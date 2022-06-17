package org.doctordrue.telegram.bot.api.keyboards.exceptions;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class OptionNotFoundException extends BaseException {

   public OptionNotFoundException(String messageText, IBotState currentState) {
      super(KeyboardErrorMessage.NOT_FOUND);
      setParameter("currentState", currentState.getMessage());
      setParameter("message", messageText);
   }
}
