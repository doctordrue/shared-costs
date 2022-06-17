package org.doctordrue.telegram.bot.api.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public interface IMessageUpdateHandler {

   void processUpdate(AbsSender absSender, Message message);

}
