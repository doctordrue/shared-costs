package org.doctordrue.telegram.bot.api.session;

import java.util.function.UnaryOperator;

/**
 * @author Andrey_Barantsev
 * 5/27/2022
 **/
public interface SessionWorker<Key, State extends IBotState, Session extends IBotSession<State>> extends StateHandler<Key, State> {

   ISessionHolder<Key, Session> getSessionHolder();

   @Override
   default State getState(Key key) {
      return getSessionHolder().getSession(key).getState();
   }

   @Override
   default void setState(Key key, State state) {
      updateSession(key, s -> {
         s.setState(state);
         return s;
      });
   }

   default void updateSession(Key key, UnaryOperator<Session> updater) {
      Session session = this.getSessionHolder().getSession(key);
      this.getSessionHolder().persist(updater.apply(session));
   }
}
