package org.doctordrue.telegram.bot.api.session;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 6/21/2022
 **/
@FunctionalInterface
public interface StateReactionFunction<Session extends IBotSession<? extends IBotState<Session>>> {

   SendMessage apply(Session session);

   default void execute(AbsSender sender, Session session) throws TelegramApiException {
      sender.execute(this.apply(session));
   }

}