package org.doctordrue.sharedcosts.telegram.bot.commands;

import java.time.LocalDate;
import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramChatSettings;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 5/12/2022
 **/
@Component
public class InitCommand extends ManCommand {

   private final GroupService groupService;
   private final TelegramChatService telegramChatService;

   public InitCommand(GroupService groupService, TelegramChatService telegramChatService) {
      super(
              "init",
              "Инициализирует бота в новой группе и создает группу совместных расходов на сервере если такой группы еще нет. " +
                      "Поддерживаются только группы",
              "Формат:\n" +
                      "/init");
      this.groupService = groupService;
      this.telegramChatService = telegramChatService;
   }

   @Override
   public void processMessage(AbsSender absSender, Message message, String[] arguments) {
      this.execute(absSender, message.getMessageId(), message.getChat());
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      // nothing to do
   }

   public void execute(AbsSender absSender, Integer messageId, Chat chat) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString()).disableNotification(true);
      if (chat.isGroupChat() || chat.isSuperGroupChat()) {
         TelegramChatSettings settings = this.telegramChatService.getOrCreate(chat.getId());
         String chatName = chat.getTitle();
         String description = chat.getDescription();
         if (settings.hasGroupAssociated()) {
            BotStatus status = settings.getBotStatus();
            if (status == null || status == BotStatus.BEFORE_START) {
               settings = this.telegramChatService.updateBotStatus(chat.getId(), BotStatus.IDLE);
            }
            builder.text("Я уже " + settings.getBotStatus().getMessage());
         } else {
            Group group = Optional.ofNullable(settings.getGroup())
                    .orElseGet(() -> this.groupService.findByName(chatName)
                            .stream()
                            .findFirst()
                            .orElseGet(() -> this.groupService.create(new Group().setName(chatName)
                                    .setDescription(description)
                                    .setStartDate(LocalDate.now()))));
            group.setTelegramChatSettings(settings);
            settings.setGroup(group);
            settings.setBotStatus(BotStatus.IDLE);
            this.groupService.update(group.getId(), group);
            this.telegramChatService.update(settings);
            builder.text("Группа совместных расходов '" + group.getName() + "' ассоциирована с чатом");
         }
      } else {
         builder.text("Привет! Данная команда не поддерживается в этом чате. Чат должен быть группой для инициализации функционала расчета расходов");
      }
      try {
         absSender.execute(builder.build());
      } catch (TelegramApiException e) {
         e.printStackTrace();
      }
   }
}
