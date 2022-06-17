package org.doctordrue.telegram.bot.common.handlers;

import org.doctordrue.telegram.bot.api.handlers.IUpdateHandler;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public class DefaultUpdateHandler implements IUpdateHandler {

   @Override
   public void processUpdate(AbsSender absSender, Update update) {
      // nothing to do
   }
}
