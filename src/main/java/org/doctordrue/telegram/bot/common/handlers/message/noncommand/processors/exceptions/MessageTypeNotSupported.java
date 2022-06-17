package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class MessageTypeNotSupported extends BaseException {

   public MessageTypeNotSupported(IBotState currentState) {
      super(UpdateProcessorError.MESSAGE_NOT_SUPPORTED);
      setParameter("currentState", currentState.getMessage());
   }
}
