package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.people.PeopleSelfService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.doctordrue.sharedcosts.utils.PasswordGeneratorUtil;
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
public class AddMeCommand extends BotCommand {

   @Autowired
   private PeopleSelfService peopleSelfService;
   @Autowired
   private PersonService personService;
   @Autowired
   private GroupService groupService;
   @Autowired
   private TelegramChatService telegramChatService;

   public AddMeCommand() {
      super(
              "addme",
              "Регистрирует человека, отправившего команду в группе совместных расходов (создает пользователя на сервере если он отсутствует)");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      StringBuilder sb = new StringBuilder();
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString());
      if (chat.isGroupChat() || chat.isSuperGroupChat()) {
         // проверить есть ли группа
         TelegramGroupChatSettings settings = this.telegramChatService.getOrCreate(chat.getId());
         if (!settings.hasGroupAssociated()) {
            builder.text("Сначала инициализируйте группу послав /init ");
         } else {
            Group group = settings.getGroup();
            Optional<Person> maybePerson = this.personService.findByTelegramId(user.getId());
            if (!maybePerson.isPresent()) {
               //register
               String identifier = StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : user.getId().toString();
               String tempPassword = PasswordGeneratorUtil.generate();
               Person person = new Person().setUsername(identifier)
                       .setTelegramId(user.getId())
                       .setFirstName(user.getFirstName())
                       .setLastName(user.getLastName())
                       .setPassword(tempPassword);
               if (this.peopleSelfService.register(person)) {
                  // registered
                  sb.append(String.format("Пользователь '%s' успешно зарегистрирован в системе. ", identifier));
               } else {
                  Person persistedPerson = this.personService.findByUsername(identifier);
                  if (persistedPerson != null && !persistedPerson.hasTelegramId()) {
                     // username already exists. need to set Telegram ID
                     persistedPerson.setTelegramId(user.getId());
                     this.personService.update(persistedPerson.getId(), persistedPerson);
                  }
               }
               maybePerson = this.personService.findByTelegramId(user.getId());
            }
            // add person to group participants
            if (maybePerson.isPresent()) {
               String username = maybePerson.get().getUsername();
               this.groupService.addParticipant(group.getId(), username);
               sb.append(String.format("Пользователь %s стал участником группы %s", username, group.getName()));
            } else {
               sb.append("Ошибка. Невозможно зарегистрировать пользователя в системе");
            }
            builder.text(sb.toString());
         }
      } else {
         builder.text("Данная команда не поддерживается в этом чате");
      }
      try {
         absSender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }

   }
}
