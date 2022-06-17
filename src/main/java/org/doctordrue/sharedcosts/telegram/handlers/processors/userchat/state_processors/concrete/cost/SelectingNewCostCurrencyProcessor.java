package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class SelectingNewCostCurrencyProcessor extends BaseStaticKeyboardReplyUserChatProcessor<CostAction> {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final CurrencyService currencyService;
   private final CostService costService;

   public SelectingNewCostCurrencyProcessor(UserChatSessionWorker sessionWorker,
                                            CurrencyService currencyService,
                                            CostService costService) {
      super(sessionWorker, TARGET_STATE, CostAction.class);
      this.currencyService = currencyService;
      this.costService = costService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Optional<Currency> maybeCurrency = this.currencyService.find(text);
         if (maybeCurrency.isPresent()) {
            // can create new cost
            LocalDateTime date = LocalDateTime.now();
            Group group = this.getSession(update).getSelectedGroup();
            Currency currency = maybeCurrency.get();
            String name = this.getSession(update).getTempCostName() + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            Cost cost = new Cost().setCurrency(currency)
                    .setName(name)
                    .setTotal(0.0)
                    .setGroup(group)
                    .setDatetime(date);
            Cost persistedCost = this.costService.create(cost);
            this.updateSession(update, s -> s.setSelectedCost(persistedCost)
                    .setTempCostName(null));
            return true;
         }
      }
      return false;
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      sendMessage(sender, SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Валюта не найдена"));
   }
}
