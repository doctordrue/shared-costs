package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseEntityKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class AwaitingNewCostNameProcessor extends BaseEntityKeyboardReplyUserChatProcessor<Currency> {

   private static final UserChatState TARGET_STATE = UserChatState.NEW_COST_SELECTING_CURRENCY;
   private final CostService costService;
   private final CurrencyService currencyService;

   public AwaitingNewCostNameProcessor(UserChatSessionWorker sessionWorker, CostService costService, CurrencyService currencyService) {
      super(sessionWorker, TARGET_STATE);
      this.costService = costService;
      this.currencyService = currencyService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         if (StringUtils.isNotBlank(text) && !this.costService.existsByName(text)) {
            this.updateSession(update, s -> s.setTempCostName(text));
            return true;
         }
      }
      return false;
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      sendMessage(sender, SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Некорректное имя, попробуйте еще."));
   }

   @Override
   protected Function<Collection<Currency>, ReplyKeyboard> keyboardFunction() {
      return KeyboardGeneratorUtils::selectCurrencyKeyboard;
   }

   @Override
   protected Supplier<Collection<Currency>> itemsSupplier(UserChatSession session) {
      return this.currencyService::findAll;
   }
}
