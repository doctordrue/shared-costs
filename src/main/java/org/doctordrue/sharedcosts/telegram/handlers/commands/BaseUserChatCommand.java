package org.doctordrue.sharedcosts.telegram.handlers.commands;

import java.util.function.Predicate;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.services.UserChatSessionService;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.session.ISessionHolder;
import org.doctordrue.telegram.bot.common.handlers.message.command.BaseStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/23/2022
 **/
public abstract class BaseUserChatCommand extends BaseStateCommand<Chat, UserChatState, UserChatSession> {

   private static final String NOT_IN_USER_CHAT_ERROR_MESSAGE = "Данная команда работает только в чате с ботом";
   @Autowired
   private UserChatSessionService userChatSessionService;

   public BaseUserChatCommand(String commandIdentifier, String description, Predicate<UserChatState> expectedStatePredicate, UserChatState successState) {
      super(commandIdentifier, description, expectedStatePredicate, successState);
   }

   public BaseUserChatCommand(String commandIdentifier, String description, UserChatState expectedState, UserChatState successState) {
      super(commandIdentifier, description, expectedState, successState);
   }

   private void sendNotUserChatErrorMessage(AbsSender sender, Chat chat) {
      this.sendMessage(sender, SendMessage.builder().chatId(chat.getId().toString()).text(NOT_IN_USER_CHAT_ERROR_MESSAGE));
   }

   /**
    * Defines how bot should react if command received when bot is in expected state
    *
    * @param absSender {@link AbsSender} to send the reaction to the chat
    * @param chat      {@link Chat} that received the command
    */
   protected abstract void onExpectedState(AbsSender absSender, Chat chat);

   /**
    * Defines how bot should react if command received when bot was in state that is not expected for the command
    *
    * @param absSender {@link AbsSender} to send the reaction to the chat
    * @param chat      {@link Chat} that received the command
    */
   protected abstract void onAnotherState(AbsSender absSender, Chat chat);

   @Override
   protected Chat getSessionKey(User user, Chat chat) {
      return chat;
   }

   @Override
   protected boolean filter(Chat chat) {
      return chat.isUserChat();
   }

   @Override
   protected boolean filter(User user) {
      return true;
   }

   @Override
   protected void onExpectedState(AbsSender absSender, User user, Chat chat) {
      this.onExpectedState(absSender, chat);
   }

   @Override
   protected void onAnotherState(AbsSender absSender, User user, Chat chat) {
      this.onAnotherState(absSender, chat);
   }

   @Override
   protected void onNonCompliantMessage(AbsSender absSender, User user, Chat chat) {
      sendNotUserChatErrorMessage(absSender, chat);
   }

   @Override
   public ISessionHolder<Chat, UserChatSession> getSessionHolder() {
      return this.userChatSessionService;
   }
}
