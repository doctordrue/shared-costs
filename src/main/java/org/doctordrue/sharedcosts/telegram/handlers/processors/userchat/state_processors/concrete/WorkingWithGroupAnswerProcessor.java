package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete;

import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardAnswerUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
@Component
public class WorkingWithGroupAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<GroupAction> {

   public WorkingWithGroupAnswerProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, GroupAction.class);
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
      builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
      Group selectedGroup = this.getSession(update).getSelectedGroup();
      if (newState.getOnStateKeyboard() != null) {
         builder.replyMarkup(KeyboardGeneratorUtils.generateStaticKeyboard(newState.getOnStateKeyboard())).text("Что будем делать?");
      } else if (newState == UserChatState.SELECTING_COST) {
         Set<Cost> costs = selectedGroup.getCosts();
         builder.replyMarkup(KeyboardGeneratorUtils.selectCostKeyboard(costs));
      } else if (newState == UserChatState.SELECTING_TRANSACTION) {
         Set<Transaction> transactions = selectedGroup.getTransactions();
         builder.replyMarkup(KeyboardGeneratorUtils.selectTransactionKeyboard(transactions));
      }
      sendMessage(absSender, builder);
   }
}