package org.doctordrue.sharedcosts.telegram.handlers.commands.userchat;

import org.doctordrue.sharedcosts.telegram.handlers.commands.BaseUserChatCommand;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
@Component
public class StopCommand extends BaseUserChatCommand {

   private static final String NOT_STARTED_MESSAGE_TEMPLATE = "Я еще ничего не начал делать. %s";

   public StopCommand() {
      super("stop",
              "[ЛС] прекратить работу с группами",
              s -> s != UserChatState.BEFORE_START,
              UserChatState.BEFORE_START);
   }

   protected void onAnotherState(AbsSender absSender, Chat chat) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder()
              .chatId(chat.getId().toString())
              .text(String.format(NOT_STARTED_MESSAGE_TEMPLATE, this.getState(chat).getMessage()));
      sendMessage(absSender, builder);
   }

   protected void onExpectedState(AbsSender absSender, Chat chat) {
      this.getSessionHolder().removeSession(chat);
      SendMessage.SendMessageBuilder builder = SendMessage.builder()
              .chatId(chat.getId().toString())
              .replyMarkup(ReplyKeyboardRemove.builder().removeKeyboard(true).build()).text("Сеанс окончен");
      sendMessage(absSender, builder);
   }
}
