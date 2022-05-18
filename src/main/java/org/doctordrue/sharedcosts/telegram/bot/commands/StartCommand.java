package org.doctordrue.sharedcosts.telegram.bot.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.telegram.UserChatSessionHolder;
import org.doctordrue.sharedcosts.telegram.bot.UserSession;
import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 5/11/2022
 **/
@Component
public class StartCommand extends BotCommand {

   private final PersonService personService;
   private final GroupService groupService;

   public StartCommand(PersonService personService, GroupService groupService) {
      super("/start", "Начинает работу с совместными расходами");
      this.personService = personService;
      this.groupService = groupService;
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      if (chat.isUserChat()) {
         // 1-2-1 chat with bot
         UserChatSessionHolder.getHolder().startSession(user);
         try {
            replyToUserChat(absSender, user, chat);
         } catch (TelegramApiException e) {
            throw new RuntimeException(e);
         }
      } else if (chat.isGroupChat() || chat.isSuperGroupChat()) {
         // group or supergroup
         InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardMarkupBuilder = InlineKeyboardMarkup.builder();
         keyboardMarkupBuilder.keyboardRow(
                 Arrays.asList(
                         InlineKeyboardButton.builder()
                                 .text("Создать новую трату")
                                 .callbackData("/newcost")
                                 .build(),
                         InlineKeyboardButton.builder()
                                 .text("Зарегистрировать меня")
                                 .callbackData("/addme")
                                 .build()));
         SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                 .chatId(chat.getId().toString())
                 .text("Чо нада?")
                 .replyMarkup(keyboardMarkupBuilder.build());
         try {
            absSender.execute(messageBuilder.build());
         } catch (TelegramApiException e) {
            e.printStackTrace();
         }
      }

   }

   private void replyToUserChat(AbsSender sender, User user, Chat chat) throws TelegramApiException {
      UserSession session = UserChatSessionHolder.getHolder().getSession(user);
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString());
      if (session == null || session.getUserId() == null) {
         throw new TelegramApiException("Session not found for user " + user.getUserName());
      }
      Optional<Person> maybePerson = this.personService.findByTelegramId(user.getId());
      if (!maybePerson.isPresent()) {
         sender.execute(builder.text("Вы не зарегистрированы ни в одной группе расходов\n" +
                 "Для регистрации добавьте меня в групповой чат и используйте команду /init").build());
         UserChatSessionHolder.getHolder().getSession(user).setStatus(BotStatus.BEFORE_START);
         return;
      } else {
         Set<Group> groups = maybePerson.get().getGroups();
         builder.text("Выберите группу");
         ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder()
                 .resizeKeyboard(true);
         groups.forEach(g -> keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(KeyboardButton.builder().text(g.getName()).build()))));
         keyboardBuilder.keyboardRow(new KeyboardRow(Collections.singletonList(KeyboardButton.builder().text("/cancel").build())));
         sender.execute(builder.replyMarkup(keyboardBuilder.build()).build());
         UserChatSessionHolder.getHolder().getSession(user).setStatus(BotStatus.SELECTING_COST);
      }

      if (session.getStatus() != BotStatus.BEFORE_START) {
         sender.execute(SendMessage.builder().text("Вы уже в процессе работы с общими расходами. Сейчас я " + session.getStatus().getMessage()).build());
      }

      sender.execute(SendMessage.builder().text("Начинаем работать с расходами. Выберите группу в который ву участвуете").build());
   }
}
