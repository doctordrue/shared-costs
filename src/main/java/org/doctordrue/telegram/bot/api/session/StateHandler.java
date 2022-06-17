package org.doctordrue.telegram.bot.api.session;

/**
 * @param <State> type of state
 * @param <Key>   type of session key
 * @author Andrey_Barantsev
 * 5/19/2022
 **/
public interface StateHandler<Key, State extends IBotState> {

   /**
    * Get bot state by key
    *
    * @param key
    * @return
    */
   State getState(Key key);

   /**
    * Change bot state for specified key. Persist if necessary
    *
    * @param key
    * @param state
    */
   void setState(Key key, State state);

}
