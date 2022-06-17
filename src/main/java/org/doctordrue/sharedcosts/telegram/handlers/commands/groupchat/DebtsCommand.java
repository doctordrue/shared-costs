package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import org.doctordrue.sharedcosts.business.model.debt_calculation.GroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
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
 * 5/23/2022
 **/
@Component
public class DebtsCommand extends BotCommand {

   @Autowired
   private TelegramChatService telegramChatService;
   @Autowired
   private DebtCalculationService debtCalculationService;

   public DebtsCommand() {
      super("debts", "Выводит информацию о долгах в группе");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(chat.getId().toString());
      if (chat.isGroupChat() || chat.isSuperGroupChat()) {
         TelegramGroupChatSettings settings = this.telegramChatService.getOrCreate(chat.getId());
         if (!settings.hasGroupAssociated()) {
            builder.text("Сначала нужно инициализировать группу командой /init");
         } else {
            GroupBalance balance = this.debtCalculationService.calculateGroupBalance(settings.getGroup().getId());
            if (balance.getDebts().isEmpty()) {
               builder.text("Никто никому ничего не должен");
            } else {
               StringBuilder stringBuilder = new StringBuilder();
               balance.getDebts().forEach(d -> stringBuilder
                       .append(d.getDebtor().getFullName())
                       .append(" должен ")
                       .append(d.getCreditor().getFullName())
                       .append(" ")
                       .append(d.getAmount())
                       .append(d.getCurrency().getShortName()));
               builder.text(stringBuilder.toString());
            }
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
