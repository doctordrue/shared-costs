package org.doctordrue.telegram.bot.common.handlers.message.command;

import java.util.function.Predicate;

import org.doctordrue.telegram.bot.api.handlers.command.IStateBotCommand;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @param <Key>     session key type
 * @param <State>   session state enum
 * @param <Session> session type
 * @author Andrey_Barantsev
 * 5/23/2022
 **/
public abstract class BaseStateCommand<Key, State extends IBotState, Session extends IBotSession<State>> extends BotCommand implements IStateBotCommand<Key, State>, SessionWorker<Key, State, Session> {

   private final Predicate<State> expectedStatePredicate;
   private final State successState;

   /**
    * Construct a command
    *
    * @param commandIdentifier      the unique identifier of this command (e.g. the command string to
    *                               enter into chat)
    * @param description            the description of this command
    * @param expectedStatePredicate {@link Predicate} to check if bot is expecting to receive the current command
    * @param successState           state to move bot to if command successfully executed
    */
   public BaseStateCommand(String commandIdentifier, String description,
                           Predicate<State> expectedStatePredicate,
                           State successState) {
      super(commandIdentifier, description);
      this.expectedStatePredicate = expectedStatePredicate;
      this.successState = successState;
   }

   public BaseStateCommand(String commandIdentifier, String description, State expectedState, State successState) {
      this(commandIdentifier, description, s -> s.equals(expectedState), successState);
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      if (filter(chat) && filter(user)) {
         Key sessionKey = getSessionKey(user, chat);
         State state = this.getState(sessionKey);
         if (this.expectedStatePredicate.test(state)) {
            onExpectedState(absSender, user, chat);
            this.setState(sessionKey, successState);
         } else {
            onAnotherState(absSender, user, chat);
         }
      } else {
         onNonCompliantMessage(absSender, user, chat);
      }
   }

   /**
    * Send simple text message
    *
    * @param sender
    * @param builder
    */
   protected void sendMessage(AbsSender sender, SendMessage.SendMessageBuilder builder) {
      try {
         sender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }

   /**
    * Implement to define how to get session key
    *
    * @param user telegram {@link User} who sent the command
    * @param chat telegram {@link Chat} in which command was sent
    * @return session key
    */
   protected abstract Key getSessionKey(User user, Chat chat);

   /**
    * Filter command message by the chat, e.g. if we want command only work in user chats or groups
    *
    * @param chat telegram {@link Chat} that received the command
    * @return <b>true</b> if {@link Chat} is compliant for the command
    */
   protected abstract boolean filter(Chat chat);

   /**
    * Filter command message by the user, e.g. if we want some commands to work for only specific users
    *
    * @param user telegram {@link User} that sent the command
    * @return <b>true</b> if {@link User} is compliant for the command
    */
   protected abstract boolean filter(User user);

   /**
    * Defines how bot should react if command received when bot is in expected state
    *
    * @param absSender {@link AbsSender} to send the reaction to the chat
    * @param user      {@link User} that sent the command
    * @param chat      {@link Chat} that received the command
    */
   protected abstract void onExpectedState(AbsSender absSender, User user, Chat chat);

   /**
    * Defines how bot should react if command received when bot was in state that is not expected for the command
    *
    * @param absSender {@link AbsSender} to send the reaction to the chat
    * @param user      {@link User} that sent the command
    * @param chat      {@link Chat} that received the command
    */
   protected abstract void onAnotherState(AbsSender absSender, User user, Chat chat);

   /**
    * Defines how bot should react if command doesn't apply filters defined in {@link #filter(Chat)} && {@link #filter(User)}
    *
    * @param absSender
    * @param user
    * @param chat
    */
   protected abstract void onNonCompliantMessage(AbsSender absSender, User user, Chat chat);
}
