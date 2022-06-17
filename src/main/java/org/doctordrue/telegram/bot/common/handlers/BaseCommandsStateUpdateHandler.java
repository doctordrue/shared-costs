package org.doctordrue.telegram.bot.common.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

import org.doctordrue.telegram.bot.api.handlers.IMessageUpdateHandler;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.generics.TelegramBot;

/**
 * The handler of some specific types of updates
 * Can store some states in a sessions by key
 *
 * @param <Key>     type of session key
 * @param <State>   enum with session states
 * @param <Session> type of session
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public abstract class BaseCommandsStateUpdateHandler<Key, State extends IBotState, Session extends IBotSession<State>> extends BaseStateUpdateHandler<Key, State, Session> implements IMessageUpdateHandler, ICommandRegistry {

   private final CommandRegistry commandRegistry;

   protected BaseCommandsStateUpdateHandler(SessionWorker<Key, State, Session> sessionWorker, boolean allowCommandsWithUsername, TelegramBot bot) {
      super(sessionWorker);
      this.commandRegistry = new CommandRegistry(allowCommandsWithUsername, bot::getBotUsername);
   }

   @Override
   public final void processUpdate(AbsSender absSender, Update update) {
      if (update.hasMessage()) {
         Message message = update.getMessage();
         if (message.isCommand() && !filter(message)) {
            if (!commandRegistry.executeCommand(absSender, message)) {
               //we have received a not registered command, handle it as invalid
               processInvalidCommandUpdate(absSender, update);
            }
            return;
         }
      }
      processNonCommandUpdate(absSender, update);
   }

   /**
    * Implement to define commands filter
    *
    * @param message to filter or not
    * @return
    */
   public abstract boolean filter(Message message);

   /**
    * Implement this method to react on non-registered command received by the handler
    *
    * @param absSender {@link AbsSender} to send updates (usually current bot instance)
    * @param update    @link Update} that has been received & defined as non-registered command
    */
   public abstract void processInvalidCommandUpdate(AbsSender absSender, Update update);

   /**
    * Implement this method to define how to process an update which is either don't have a {@link Message} or its {@link Message} is not a command
    *
    * @param absSender @link AbsSender} to send updates (usually current bot instance)
    * @param update    {@link Update} that has been received & defined as non-registered command
    */
   public abstract void processNonCommandUpdate(AbsSender absSender, Update update);

   @Override
   public void registerDefaultAction(BiConsumer<AbsSender, Message> defaultConsumer) {
      this.commandRegistry.registerDefaultAction(defaultConsumer);
   }

   @Override
   public boolean register(IBotCommand botCommand) {
      return this.commandRegistry.register(botCommand);
   }

   @Override
   public Map<IBotCommand, Boolean> registerAll(IBotCommand... botCommands) {
      return this.commandRegistry.registerAll(botCommands);
   }

   @Override
   public boolean deregister(IBotCommand botCommand) {
      return this.commandRegistry.deregister(botCommand);
   }

   @Override
   public Map<IBotCommand, Boolean> deregisterAll(IBotCommand... botCommands) {
      return this.commandRegistry.deregisterAll(botCommands);
   }

   @Override
   public Collection<IBotCommand> getRegisteredCommands() {
      return this.commandRegistry.getRegisteredCommands();
   }

   @Override
   public IBotCommand getRegisteredCommand(String commandIdentifier) {
      return this.commandRegistry.getRegisteredCommand(commandIdentifier);
   }
}
