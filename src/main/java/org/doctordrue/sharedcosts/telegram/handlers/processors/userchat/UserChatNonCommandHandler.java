package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.SelectingGroupProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.WorkingWithGroupAnswerProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.SelectingCostProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.SelectingNewCostCurrencyProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.WorkingWithCostAnswerProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation.AwaitingNewParticipationNameProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation.AwaitingNewParticipationPriceProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation.SelectingNewParticipationWhoProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment.AwaitingNewPaymentAmountProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment.SelectingNewPaymentWhoProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost.*;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction.*;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.handler.BaseNonCommandHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class UserChatNonCommandHandler extends BaseNonCommandHandler<Chat, UserChatState, UserChatSession> {

   public UserChatNonCommandHandler(UserChatSessionWorker sessionWorker,
                                    WorkingWithGroupAnswerProcessor workingWithGroupProcessor,
                                    SelectingGroupProcessor selectingGroupProcessor,
                                    SelectingNewCostCurrencyProcessor selectingNewCostCurrencyProcessor,
                                    SelectingCostProcessor selectingCostProcessor,
                                    WorkingWithCostAnswerProcessor workingWithCostProcessor,
                                    AwaitingNewPaymentAmountProcessor awaitingNewPaymentAmountProcessor,
                                    SelectingNewPaymentWhoProcessor selectingNewPaymentWhoProcessor,
                                    AwaitingNewParticipationNameProcessor awaitingNewParticipationNameProcessor,
                                    AwaitingNewParticipationPriceProcessor awaitingNewParticipationPriceProcessor,
                                    SelectingNewParticipationWhoProcessor selectingNewParticipationWhoProcessor,
                                    SelectingNewTransactionCurrencyProcessor selectingNewTransactionCurrencyProcessor,
                                    AwaitingNewTransactionAmountProcessor awaitingNewTransactionAmountProcessor,
                                    SelectingNewTransactionFromProcessor selectingNewTransactionFromProcessor,
                                    SelectingTransactionProcessor selectingTransactionProcessor,
                                    WorkingWithTransactionAnswerProcessor workingWithTransactionAnswerProcessor,
                                    ProcessCostAwaitingNameProcessor processCostAwaitingNameProcessor,
                                    ProcessCostSelectingCurrencyProcessor processCostSelectingCurrencyProcessor,
                                    ProcessCostSelectingPayerProcessor processCostSelectingPayerProcessor,
                                    ProcessCostActionProcessor processCostActionProcessor,
                                    ProcessCostAwaitingItemNameProcessor processCostAwaitingItemNameProcessor,
                                    ProcessCostAwaitingItemPriceProcessor processCostAwaitingItemPriceProcessor) {
      super(sessionWorker);
      this.registerProcessor(UserChatState.SELECTING_GROUP, selectingGroupProcessor);
      this.registerProcessor(UserChatState.WORKING_WITH_GROUP, workingWithGroupProcessor);
      this.registerProcessor(UserChatState.NEW_COST_SELECTING_CURRENCY, selectingNewCostCurrencyProcessor);
      this.registerProcessor(UserChatState.SELECTING_COST, selectingCostProcessor);
      this.registerProcessor(UserChatState.WORKING_WITH_COST, workingWithCostProcessor);
      this.registerProcessor(UserChatState.NEW_PAYMENT_AWAITING_AMOUNT, awaitingNewPaymentAmountProcessor);
      this.registerProcessor(UserChatState.NEW_PAYMENT_SELECTING_WHO, selectingNewPaymentWhoProcessor);
      this.registerProcessor(UserChatState.NEW_PARTICIPATION_AWAITING_NAME, awaitingNewParticipationNameProcessor);
      this.registerProcessor(UserChatState.NEW_PARTICIPATION_AWAITING_PRICE, awaitingNewParticipationPriceProcessor);
      this.registerProcessor(UserChatState.NEW_PARTICIPATION_AWAITING_WHO, selectingNewParticipationWhoProcessor);
      this.registerProcessor(UserChatState.NEW_TRANSACTION_SELECTING_CURRENCY, selectingNewTransactionCurrencyProcessor);
      this.registerProcessor(UserChatState.NEW_TRANSACTION_AWAITING_AMOUNT, awaitingNewTransactionAmountProcessor);
      this.registerProcessor(UserChatState.NEW_TRANSACTION_SELECTING_FROM, selectingNewTransactionFromProcessor);
      this.registerProcessor(UserChatState.SELECTING_TRANSACTION, selectingTransactionProcessor);
      this.registerProcessor(UserChatState.WORKING_WITH_TRANSACTION, workingWithTransactionAnswerProcessor);

      this.registerProcessor(UserChatState.PROCESS_COST_AWAITING_NAME, processCostAwaitingNameProcessor);
      this.registerProcessor(UserChatState.PROCESS_COST_SELECTING_CURRENCY, processCostSelectingCurrencyProcessor);
      this.registerProcessor(UserChatState.PROCESS_COST_SELECTING_PAYER, processCostSelectingPayerProcessor);
      this.registerProcessor(UserChatState.PROCESS_COST_SELECTING_ACTION, processCostActionProcessor);
      this.registerProcessor(UserChatState.PROCESS_COST_AWAITING_ITEM_NAME, processCostAwaitingItemNameProcessor);
      this.registerProcessor(UserChatState.PROCESS_COST_AWAITING_ITEM_PRICE, processCostAwaitingItemPriceProcessor);
   }

   @Override
   protected Chat getSessionKey(Update update) {
      return update.getMessage().getChat();
   }

   @Override
   protected void onNoProcessorsFound(AbsSender absSender, Update update) {
      sendMessage(absSender, SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Не знаю что делать в этой ситуации: " + this.getState(update).name()));
   }
}
