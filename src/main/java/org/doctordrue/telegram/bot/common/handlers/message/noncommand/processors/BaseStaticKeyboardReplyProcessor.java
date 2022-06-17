package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

/**
 * @author Andrey_Barantsev
 * 6/10/2022
 **/
public abstract class BaseStaticKeyboardReplyProcessor<Key, State extends IBotState, Session extends IBotSession<State>, Keyboard extends Enum<? extends KeyboardOption<State>>>
        extends BaseKeyboardReplyProcessor<Key, State, Session> {

   private final Class<Keyboard> replyKeyboardEnum;

   /**
    * @param sessionWorker
    * @param targetState       bot state to move to if <b>expectedUpdatePredicate</b> was successfully tested
    * @param replyKeyboardEnum enum with bot's reply keyboard options
    */
   public BaseStaticKeyboardReplyProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState, Class<Keyboard> replyKeyboardEnum) {
      super(sessionWorker, targetState);
      this.replyKeyboardEnum = replyKeyboardEnum;
   }

   @Override
   protected ReplyKeyboardMarkup getKeyboard(Session session) {
      return KeyboardGeneratorUtils.generateStaticKeyboard(this.replyKeyboardEnum);
   }
}
