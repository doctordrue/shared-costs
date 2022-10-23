package org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat;

import org.doctordrue.sharedcosts.business.model.debt_calculation.GroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.handlers.commands.BaseGroupChatCommand;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Andrey_Barantsev
 * 5/23/2022
 **/
@Component
public class DebtsCommand extends BaseGroupChatCommand {

    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    private DebtCalculationService debtCalculationService;

    public DebtsCommand() {
        super("debts", "[чат группы] вывести информацию о долгах в группе");
    }

    @Override
    public void execute(SendMessage sendMessage, User user, Chat chat) {
        TelegramGroupChatSettings settings = this.telegramChatService.getOrCreate(chat.getId());
        if (!settings.hasGroupAssociated()) {
            sendMessage.setText("Сначала нужно инициализировать группу командой /init");
        } else {
            GroupBalance balance = this.debtCalculationService.calculateGroupBalance(settings.getGroup().getId());
            if (balance.getDebts().isEmpty()) {
                sendMessage.setText("Никто никому ничего не должен");
            } else {
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                sendMessage.setText(balance.toTelegramString());
            }
        }
    }
}
