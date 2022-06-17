package org.doctordrue.telegram.bot.api.handlers.noncommand;

import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.StateHandler;

/**
 * @author Andrey_Barantsev
 * 5/27/2022
 **/
public interface StateProcessor<Key, State extends IBotState> extends StateHandler<Key, State>, UpdateProcessor {

}