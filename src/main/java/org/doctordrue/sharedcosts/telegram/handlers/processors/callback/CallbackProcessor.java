package org.doctordrue.sharedcosts.telegram.handlers.processors.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class CallbackProcessor {

   public void execute(AbsSender absSender, CallbackQuery callbackQuery) {

   }

   /**
    * Override to filter messages
    *
    * @param message
    * @return
    */
   public boolean filter(Message message) {
      return false;
   }

}
