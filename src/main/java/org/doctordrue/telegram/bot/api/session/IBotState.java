package org.doctordrue.telegram.bot.api.session;

/**
 * To implement by <b>enum</b> with statuses
 *
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public interface IBotState<Session extends IBotSession<? extends IBotState<Session>>> {

   String getMessage();

   StateReactionFunction<Session> getOnStateReaction();
}
