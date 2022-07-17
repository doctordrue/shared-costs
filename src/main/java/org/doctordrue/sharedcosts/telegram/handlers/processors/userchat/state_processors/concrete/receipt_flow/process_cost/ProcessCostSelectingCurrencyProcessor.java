package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

import java.util.Optional;

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
 * 6/24/2022
 **/
@Component
public class ProcessCostSelectingCurrencyProcessor extends BaseSingleStateUserChatProcessor {

   public ProcessCostSelectingCurrencyProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.PROCESS_COST_SELECTING_PAYER);
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Валюта чека: %s", session.getCurrency().getFullName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Optional<Currency> maybeCurrency = session.getAvailableCurrencies().stream().filter(c -> c.getShortName().equals(text)).findFirst();
      if (maybeCurrency.isPresent()) {
         // can create new cost
         this.updateSession(session, s -> s.setCurrency(maybeCurrency.get()));
      } else {
         throw new CurrencyNotFoundException(text);
      }
   }
}
