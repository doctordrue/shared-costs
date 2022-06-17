package org.doctordrue.telegram.bot.common.handlers;

import java.util.function.UnaryOperator;

import org.doctordrue.telegram.bot.api.handlers.IUpdateHandler;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 5/19/2022
 **/
public abstract class BaseStateUpdateHandler<Key, State extends IBotState, Session extends IBotSession<State>> implements IUpdateHandler {

   private final SessionWorker<Key, State, Session> sessionWorker;

   public BaseStateUpdateHandler(SessionWorker<Key, State, Session> sessionWorker) {
      this.sessionWorker = sessionWorker;
   }

   public State getState(Update update) {
      return this.sessionWorker.getState(this.getSessionKey(update));
   }

   public void setState(Update update, State state) {
      this.sessionWorker.setState(getSessionKey(update), state);
   }

   public void updateSession(Update update, UnaryOperator<Session> updater) {
      this.sessionWorker.updateSession(this.getSessionKey(update), updater);
   }

   public Session getSession(Update update) {
      return this.sessionWorker.getSessionHolder().getSession(getSessionKey(update));
   }

   /**
    * Defines how to extract session key from {@link Update} received
    *
    * @param update {@link Update} received
    * @return session key
    */
   protected abstract Key getSessionKey(Update update);
}
