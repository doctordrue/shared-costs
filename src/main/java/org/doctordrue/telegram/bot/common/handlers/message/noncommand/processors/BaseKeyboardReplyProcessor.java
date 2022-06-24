package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/15/2022
 **/
public abstract class BaseKeyboardReplyProcessor<Key, State extends IBotState<Session>, Session extends IBotSession<State>> extends BaseSingleStateUpdateProcessor<Key, State, Session> {

   /**
    * @param sessionWorker
    * @param targetState   bot state to move to if <b>expectedUpdatePredicate</b> was successfully tested
    */
   public BaseKeyboardReplyProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected void onExpectedInput(AbsSender sender, Update update) {
      Chat chat = update.getMessage().getChat();
      sendMessage(sender, SendMessage.builder().chatId(chat.getId().toString())
              .replyMarkup(getKeyboard(getSession(update)))
              .text(getExpectedInputMessage(getSession(update))));
   }

   protected String getExpectedInputMessage(Session session) {
      return this.getTargetState().getMessage();
   }

   /**
    * Defines how to generate keyboard
    *
    * @return instance of {@link ReplyKeyboardMarkup} to be used in bot's reply
    */
   protected abstract ReplyKeyboard getKeyboard(Session session);

}
