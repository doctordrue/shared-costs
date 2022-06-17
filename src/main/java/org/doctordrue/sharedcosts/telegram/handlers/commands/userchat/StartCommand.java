package org.doctordrue.sharedcosts.telegram.handlers.commands.userchat;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.telegram.handlers.commands.BaseUserChatCommand;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
@Component
public class StartCommand extends BaseUserChatCommand {

   private static final String ALREADY_IN_WORK_TEMPLATE = "Я уже в процессе: %s";

   @Autowired
   private PersonService personService;

   public StartCommand() {
      super("start", "Начинает работу с группами совместных расходов",
              UserChatState.BEFORE_START,
              UserChatState.SELECTING_GROUP);
   }

   @Override
   protected void onExpectedState(AbsSender absSender, User user, Chat chat) {
      Person person = this.personService
              .findByTelegramId(user.getId())
              .orElseGet(() -> this.personService.addTelegramUser(user));
      SendMessage.SendMessageBuilder builder = SendMessage.builder()
              .chatId(chat.getId().toString())
              .replyMarkup(KeyboardGeneratorUtils.selectGroupsKeyboard(person.getGroups())).text("Выберите группу");
      sendMessage(absSender, builder);
   }

   @Override
   protected void onExpectedState(AbsSender absSender, Chat chat) {

   }

   @Override
   protected void onAnotherState(AbsSender absSender, Chat chat) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder()
              .chatId(chat.getId().toString())
              .text(String.format(ALREADY_IN_WORK_TEMPLATE, this.getState(chat).getMessage()));
      sendMessage(absSender, builder);
   }
}
