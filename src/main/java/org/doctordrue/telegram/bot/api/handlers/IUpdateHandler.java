package org.doctordrue.telegram.bot.api.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public interface IUpdateHandler {

   void processUpdate(AbsSender absSender, Update update);

}
