package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import java.time.LocalDate;
import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
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
 * 5/12/2022
 **/
@Component
public class InitCommand extends BotCommand {
   @Autowired
   private GroupService groupService;
   @Autowired
   private TelegramChatService telegramChatService;

   public InitCommand() {
      super(
              "init",
              "Инициализирует бота в новой группе и создает группу совместных расходов на сервере если такой группы еще нет. " +
                      "Поддерживаются только группы");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString()).disableNotification(true);
      if (chat.isGroupChat() || chat.isSuperGroupChat()) {
         TelegramGroupChatSettings settings = this.telegramChatService.getOrCreate(chat.getId());
         String chatName = chat.getTitle();
         String description = chat.getDescription();
         if (!settings.hasGroupAssociated()) {
            // need to create group
            Group group = Optional.ofNullable(settings.getGroup())
                    .orElseGet(() -> this.groupService.findByName(chatName)
                            .stream()
                            .findFirst()
                            .orElseGet(() -> this.groupService.create(new Group()
                                    .setName(chatName)
                                    .setDescription(description)
                                    .setStartDate(LocalDate.now()))));
            settings.setGroup(group);
            this.groupService.update(group.getId(), group);
            this.telegramChatService.update(settings);
            builder.text("Группа совместных расходов '" + group.getName() + "' ассоциирована с чатом.");
         } else {
            // there is a group
            builder.text("Группа совместных расходов '" + settings.getGroup().getName() + "' уже ассоциирована с чатом.");
         }
      } else {
         builder.text("Привет! Данная команда не поддерживается в этом чате. Чат должен быть группой для инициализации функционала расчета расходов");
      }
      try {
         absSender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }

}
