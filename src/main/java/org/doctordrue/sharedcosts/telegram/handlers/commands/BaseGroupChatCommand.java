package org.doctordrue.sharedcosts.telegram.handlers.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public abstract class BaseGroupChatCommand extends BotCommand {

    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    public BaseGroupChatCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public final void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage sendMessage = SendMessage.builder().text("").chatId(chat.getId().toString()).build();
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            execute(sendMessage, user, chat);
        } else {
            sendMessage.setText("Данная команда не поддерживается в этом чате");
        }
        try {
            absSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void execute(SendMessage sendMessage, User user, Chat chat);
}
