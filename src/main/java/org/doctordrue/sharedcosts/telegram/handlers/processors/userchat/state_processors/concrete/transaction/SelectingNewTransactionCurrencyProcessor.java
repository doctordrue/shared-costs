package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.exceptions.currency.CurrencyNotFoundException;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseNoKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/20/2022
 **/
@Component
public class SelectingNewTransactionCurrencyProcessor extends BaseNoKeyboardReplyUserChatProcessor {

   private final CurrencyService currencyService;

   public SelectingNewTransactionCurrencyProcessor(UserChatSessionWorker sessionWorker, CurrencyService currencyService) {
      super(sessionWorker, UserChatState.NEW_TRANSACTION_AWAITING_AMOUNT);
      this.currencyService = currencyService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Currency currency = this.currencyService.find(text).orElseThrow(() -> new CurrencyNotFoundException(text));
         this.updateSession(update, s -> s.setCurrency(currency));
         return true;
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
