package org.doctordrue.sharedcosts.telegram.handlers.commands;

import org.doctordrue.sharedcosts.telegram.SharedCostsBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand.getHelpText;

@Component
public class MyHelpCommand extends BotCommand {

    public MyHelpCommand() {
        super("help",
                "вывести справку по командам, доступным в текущем чате");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (absSender instanceof ICommandRegistry) {
            ICommandRegistry bot = (SharedCostsBot) absSender;
            List<IBotCommand> availableCommands = new ArrayList<>();
            if (chat.isGroupChat() || chat.isSuperGroupChat()) {
                availableCommands.addAll(bot.getRegisteredCommands().stream()
                        .filter(BaseGroupChatCommand.class::isInstance)
                        .collect(Collectors.toList()));
            } else if (chat.isUserChat()) {
                // return list of avaliable user chat commands
                availableCommands.addAll(bot.getRegisteredCommands().stream()
                        .filter(BaseUserChatCommand.class::isInstance)
                        .collect(Collectors.toList()));
            }
            availableCommands.addAll(bot.getRegisteredCommands().stream()
                    .filter(c -> !(c instanceof BaseUserChatCommand) && !(c instanceof BaseGroupChatCommand))
                    .collect(Collectors.toList()));

            String reply = getHelpText(availableCommands);
            try {
                absSender.execute(SendMessage.builder().chatId(chat.getId().toString()).text(reply).parseMode("HTML").build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
