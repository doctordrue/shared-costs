package org.doctordrue.sharedcosts.telegram.bot.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.ManCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class AddCostCommand extends ManCommand {

   public AddCostCommand() {
      super(
              "addcost",
              "Создает новую статью совместных расходов для группы и переводит бота в режим работы со статьей расходов",
              "Формат\n" +
                      "/addcost &lt;названии статьи расходов&gt; [&lt;сумма&gt;]");
   }

   @Override
   public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

      if (arguments.length < 1 || arguments.length > 2) {

      }
   }
}
