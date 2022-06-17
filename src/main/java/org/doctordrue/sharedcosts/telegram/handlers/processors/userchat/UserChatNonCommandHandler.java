package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.SelectingGroupProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.WorkingWithGroupAnswerProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.AwaitingNewCostNameProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.SelectingCostProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.SelectingNewCostCurrencyProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.WorkingWithCostAnswerProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment.AwaitingNewPaymentAmountProcessor;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment.SelectingNewPaymentWhoProcessor;
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
                                    AwaitingNewCostNameProcessor awaitingNewCostNameProcessor,
                                    SelectingCostProcessor selectingCostProcessor,
                                    WorkingWithCostAnswerProcessor workingWithCostProcessor,
                                    AwaitingNewPaymentAmountProcessor awaitingNewPaymentAmountProcessor,
                                    SelectingNewPaymentWhoProcessor selectingNewPaymentWhoProcessor) {
      super(sessionWorker);
      this.registerProcessor(UserChatState.SELECTING_GROUP, selectingGroupProcessor);
      this.registerProcessor(UserChatState.WORKING_WITH_GROUP, workingWithGroupProcessor);
      this.registerProcessor(UserChatState.NEW_COST_SELECTING_CURRENCY, selectingNewCostCurrencyProcessor);
      this.registerProcessor(UserChatState.NEW_COST_AWAITING_NAME, awaitingNewCostNameProcessor);
      this.registerProcessor(UserChatState.SELECTING_COST, selectingCostProcessor);
      this.registerProcessor(UserChatState.WORKING_WITH_COST, workingWithCostProcessor);
      this.registerProcessor(UserChatState.NEW_PAYMENT_AWAITING_AMOUNT, awaitingNewPaymentAmountProcessor);
      this.registerProcessor(UserChatState.NEW_PAYMENT_SELECTING_WHO, selectingNewPaymentWhoProcessor);
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
