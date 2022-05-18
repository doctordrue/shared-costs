package org.doctordrue.sharedcosts.telegram.bot.processors.other;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/11/2022
 **/
public interface INonCommandUpdateProcessor {

   void execute(AbsSender absSender, Update update);
}
