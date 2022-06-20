package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete;

import java.util.List;
import java.util.Set;

import org.doctordrue.sharedcosts.business.model.debt_calculation.GroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardAnswerUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
@Component
public class WorkingWithGroupAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<GroupAction> {

   private final CurrencyService currencyService;
   private final DebtCalculationService debtCalculationService;

   public WorkingWithGroupAnswerProcessor(UserChatSessionWorker sessionWorker, CurrencyService currencyService, DebtCalculationService debtCalculationService) {
      super(sessionWorker, GroupAction.class);
      this.currencyService = currencyService;
      this.debtCalculationService = debtCalculationService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
      builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
      Group selectedGroup = this.getSession(update).getSelectedGroup();
      if (newState == UserChatState.SELECTING_COST) {
         Set<Cost> costs = selectedGroup.getCosts();
         builder.replyMarkup(KeyboardGeneratorUtils.selectCostKeyboard(costs));
      } else if (newState == UserChatState.SELECTING_TRANSACTION) {
         Set<Transaction> transactions = selectedGroup.getTransactions();
         builder.replyMarkup(KeyboardGeneratorUtils.selectTransactionKeyboard(transactions));
      } else if (newState == UserChatState.NEW_TRANSACTION_SELECTING_CURRENCY) {
         List<Currency> currencies = this.currencyService.findAll();
         builder.replyMarkup(KeyboardGeneratorUtils.selectCurrencyKeyboard(currencies));
      } else if (newState == UserChatState.WORKING_WITH_GROUP) {
         GroupBalance balance = this.debtCalculationService.calculateGroupBalance(this.getSession(update).getSelectedGroup().getId());
         builder.parseMode(ParseMode.MARKDOWN);
         builder.text(balance.toTelegramString());
      }
      if (newState.getOnStateKeyboard() != null) {
         builder.replyMarkup(KeyboardGeneratorUtils.generateStaticKeyboard(newState.getOnStateKeyboard()));
      }
      sendMessage(absSender, builder);
   }
}