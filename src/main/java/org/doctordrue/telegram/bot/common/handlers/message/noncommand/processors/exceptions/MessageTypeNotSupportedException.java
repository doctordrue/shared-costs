package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class MessageTypeNotSupportedException extends BaseException {

   public MessageTypeNotSupportedException() {
      super(UpdateProcessorError.MESSAGE_NOT_SUPPORTED);
   }
}
