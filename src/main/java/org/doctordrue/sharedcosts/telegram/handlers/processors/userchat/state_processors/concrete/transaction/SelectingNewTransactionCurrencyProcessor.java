package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.currency.CurrencyNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/20/2022
 **/
@Component
public class SelectingNewTransactionCurrencyProcessor extends BaseSingleStateUserChatProcessor {

   private final CurrencyService currencyService;

   public SelectingNewTransactionCurrencyProcessor(UserChatSessionWorker sessionWorker, CurrencyService currencyService) {
      super(sessionWorker, UserChatState.NEW_TRANSACTION_AWAITING_AMOUNT);
      this.currencyService = currencyService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Добавляем перевод в валюте '%s'";
      return String.format(template, session.getCurrency().getFullName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Currency currency = this.currencyService.find(text).orElseThrow(() -> new CurrencyNotFoundException(text));
      this.updateSession(session, s -> s.setCurrency(currency));
   }
}
