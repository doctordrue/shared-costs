package org.doctordrue.sharedcosts.telegram.bot.processors.callback;

import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class CallbackProcessor {

   private BotStatus botStatus;

   public void execute(AbsSender absSender, CallbackQuery callbackQuery) {
      Message message = callbackQuery.getMessage();

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
