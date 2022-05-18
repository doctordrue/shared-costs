package org.doctordrue.sharedcosts.telegram.bot.commands;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.people.PeopleSelfService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.utils.PasswordGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
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
public class AddMeCommand extends ManCommand {

   @Autowired
   private PeopleSelfService peopleSelfService;
   @Autowired
   private PersonService personService;
   @Autowired
   private GroupService groupService;

   public AddMeCommand() {
      super(
              "addme",
              "Регистрирует человека, отправившего команду в группе совместных расходов (создает пользователя на сервере если он отсутствует)",
              "Формат:\n" +
                      "/addme");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      StringBuilder sb = new StringBuilder();
      SendMessage replyToGroup = new SendMessage();
      replyToGroup.setChatId(chat.getId().toString());
      String title = chat.getTitle();
      Optional<Group> optionalGroup = Optional.empty();
      if (chat.isGroupChat()) {
         // проверить есть ли группа
         optionalGroup = this.groupService.findByName(title).stream().findFirst();
      }
      if (!optionalGroup.isPresent()) {
         sb.append("Сначала инициализируйте группу послав /init ");
      } else {
         String identifier = StringUtils.isNotEmpty(user.getUserName()) ?
                 user.getUserName() : user.getId().toString();
         String tempPassword = PasswordGeneratorUtil.generate();
         Person person = new Person().setEmail(identifier)
                 .setTelegramId(user.getId())
                 .setFirstName(user.getFirstName())
                 .setLastName(user.getLastName())
                 .setPassword(tempPassword);
         if (this.peopleSelfService.register(person)) {
            // new person registered
            sb.append(String.format("Пользователь '%s' успешно зарегистрирован в системе. ", identifier));
         } else {
            sb.append("Пользователь уже зарегистрирован!");
            Person persistedPerson = this.personService.loadUserByUsername(identifier);
            if (!persistedPerson.hasTelegramId()) {
               persistedPerson.setTelegramId(user.getId());
               this.personService.update(persistedPerson.getId(), persistedPerson);
            }
         }
         this.groupService.addParticipant(optionalGroup.get().getId(), identifier);
         sb.append(String.format("Пользователь %s стал участником группы %s", identifier, optionalGroup.get().getName()));
      }

      replyToGroup.setText(sb.toString());
      try {
         absSender.execute(replyToGroup);
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }

   }
}
