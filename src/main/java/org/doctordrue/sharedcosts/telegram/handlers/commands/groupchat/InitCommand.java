package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.handlers.commands.BaseGroupChatCommand;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Andrey_Barantsev
 * 5/12/2022
 **/
@Component
public class InitCommand extends BaseGroupChatCommand {
    @Autowired
    private GroupService groupService;
    @Autowired
    private TelegramChatService telegramChatService;

    public InitCommand() {
        super(
                "init",
                "[чат группы] создать группу совместных расходов для чата");
    }

    @Override
    public void execute(SendMessage sendMessage, User user, Chat chat) {
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
            sendMessage.setText("Группа совместных расходов '" + group.getName() + "' ассоциирована с чатом.");
        } else {
            // there is a group
            sendMessage.setText("Группа совместных расходов '" + settings.getGroup().getName() + "' уже ассоциирована с чатом.");
        }

    }

}
