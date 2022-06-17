package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

/**
 * @author Andrey_Barantsev
 * 6/10/2022
 **/
public abstract class BaseNoKeyboardReplyProcessor<Key, State extends IBotState, Session extends IBotSession<State>> extends BaseKeyboardReplyProcessor<Key, State, Session> {

   /**
    * @param sessionWorker
    * @param targetState   bot state to move to if <b>expectedUpdatePredicate</b> was successfully tested
    */
   public BaseNoKeyboardReplyProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected ReplyKeyboardRemove getKeyboard(Session session) {
      return KeyboardGeneratorUtils.removeKeyboard();
   }
}
