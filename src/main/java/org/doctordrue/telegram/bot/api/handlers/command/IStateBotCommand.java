package org.doctordrue.telegram.bot.api.handlers.command;

import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.StateHandler;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

/**
 * @author Andrey_Barantsev
 * 5/23/2022
 **/
public interface IStateBotCommand<Key, State extends IBotState> extends IBotCommand, StateHandler<Key, State> {

}
