package org.doctordrue.telegram.bot.api.handlers.noncommand;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/27/2022
 **/
public interface UpdateProcessor {

   void processUpdate(AbsSender absSender, Update update);
}