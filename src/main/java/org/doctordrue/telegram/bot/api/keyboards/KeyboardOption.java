package org.doctordrue.telegram.bot.api.keyboards;

import org.doctordrue.telegram.bot.api.session.IBotState;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public interface KeyboardOption<State extends IBotState> {

   String getOption();

   State getTargetState();
}