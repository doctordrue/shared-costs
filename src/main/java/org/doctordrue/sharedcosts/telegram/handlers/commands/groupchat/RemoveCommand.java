package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import java.util.Optional;

import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
@Component
public class RemoveCommand extends BotCommand {

   @Autowired
   private TelegramChatService telegramChatService;

   public RemoveCommand() {
      super("remove", "Удаляет привязку текущего чата к группе совместных расходов (сама группа останется на сервере)");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString()).disableNotification(true);
      Optional<TelegramGroupChatSettings> maybeSettings = this.telegramChatService.findByChatId(chat.getId());
      if (maybeSettings.isPresent()) {
         if (maybeSettings.get().hasGroupAssociated()) {
            // need to remove group association
            String groupName = maybeSettings.get().getGroup().getName();
            this.telegramChatService.remove(chat.getId());
            builder.text("Чат успешно отвязан от группы совместных расходов " + groupName);
         } else {
            builder.text("Отсутствует привязка чата к группе совместных расходов");
         }
      } else {
         builder.text("Чат еще не был инициализирован");
      }
      try {
         absSender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }
}
