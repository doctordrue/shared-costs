package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.handlers.commands.BaseGroupChatCommand;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
@Component
public class RemoveCommand extends BaseGroupChatCommand {

   @Autowired
   private TelegramChatService telegramChatService;

   public RemoveCommand() {
      super("remove", "[чат группы] удалить привязку группы совместных расходов к чаты в Telegram");
   }

   @Override
   public void execute(SendMessage sendMessage, User user, Chat chat) {
      sendMessage.disableNotification();
      Optional<TelegramGroupChatSettings> maybeSettings = this.telegramChatService.findByChatId(chat.getId());
      if (maybeSettings.isPresent()) {
         if (maybeSettings.get().hasGroupAssociated()) {
            // need to remove group association
            String groupName = maybeSettings.get().getGroup().getName();
            this.telegramChatService.remove(chat.getId());
            sendMessage.setText("Чат успешно отвязан от группы совместных расходов " + groupName);
         } else {
            sendMessage.setText("Отсутствует привязка чата к группе совместных расходов");
         }
      } else {
         sendMessage.setText("Чат еще не был инициализирован");
      }
   }
}
