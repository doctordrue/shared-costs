package org.doctordrue.sharedcosts.telegram.bot.commands;

import org.doctordrue.sharedcosts.telegram.UserChatSessionHolder;
import org.doctordrue.sharedcosts.telegram.bot.UserSession;
import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
@Component
public class CancelCommand extends BotCommand {

   public CancelCommand() {
      super("cancel", "отменяет процесс работы с расходами");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      if (chat.isUserChat()) {
         UserSession session = UserChatSessionHolder.getHolder().getSession(user);
         if (session == null) {
            removeKeyboard(absSender, chat);
            return;
         }
         switch (session.getStatus()) {
            case SELECTING_COST:
            default:
               session.setStatus(BotStatus.BEFORE_START);
               UserChatSessionHolder.getHolder().removeSession(user);
               removeKeyboard(absSender, chat);
               break;
         }
      }
   }

   private void removeKeyboard(AbsSender sender, Chat chat) {
      try {
         sender.execute(SendMessage.builder()
                 .chatId(chat.getId().toString())
                 .text("Окааай...")
                 .replyMarkup(ReplyKeyboardRemove.builder()
                         .removeKeyboard(true)
                         .build())
                 .build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }
}
