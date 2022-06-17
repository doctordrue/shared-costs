package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class SomethingWentWrongException extends BaseException {

   public SomethingWentWrongException(IBotState currentState) {
      super(UpdateProcessorError.SOMETHING_WENT_WRONG);
      setParameter("currentState", currentState.getMessage());
   }
}
