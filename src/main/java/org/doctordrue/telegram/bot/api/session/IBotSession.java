package org.doctordrue.telegram.bot.api.session;

/**
 * @param <State> session state enum
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public interface IBotSession<State extends IBotState> {

   /**
    * Get session state
    *
    * @return
    */
   State getState();

   /**
    * Changes session state
    *
    * @param state
    * @return
    */
   IBotSession<State> setState(State state);
}
