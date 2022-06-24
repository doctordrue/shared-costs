package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
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
 * 6/9/2022
 **/
@Component
public class SelectingNewCostCurrencyProcessor extends BaseSingleStateUserChatProcessor {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final CurrencyService currencyService;
   private final CostService costService;

   public SelectingNewCostCurrencyProcessor(UserChatSessionWorker sessionWorker,
                                            CurrencyService currencyService,
                                            CostService costService) {
      super(sessionWorker, TARGET_STATE);
      this.currencyService = currencyService;
      this.costService = costService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Выбрана валюта '%s'", session.getCurrency().getFullName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Optional<Currency> maybeCurrency = this.currencyService.find(text);
      if (maybeCurrency.isPresent()) {
         // can create new cost
         LocalDateTime date = LocalDateTime.now();
         Group group = session.getSelectedGroup();
         Currency currency = maybeCurrency.get();
         String name = session.getTempCostName() + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
         Cost cost = new Cost().setCurrency(currency)
                 .setName(name)
                 .setTotal(0.0)
                 .setGroup(group)
                 .setDatetime(date);
         Cost persistedCost = this.costService.create(cost);
         this.updateSession(session, s -> s.setSelectedCost(persistedCost)
                 .setTempCostName(null));
      } else {
         throw new CurrencyNotFoundException(text);
      }
   }
}
