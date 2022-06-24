package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.business.services.dataaccess.TransactionService;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.TransactionAction;
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
 * 6/20/2022
 **/
@Component
public class WorkingWithTransactionAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<TransactionAction> {

   private final TransactionService transactionService;

   public WorkingWithTransactionAnswerProcessor(UserChatSessionWorker sessionWorker, TransactionService transactionService) {
      super(sessionWorker, TransactionAction.class);
      this.transactionService = transactionService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      Transaction selectedTransaction = this.getSession(update).getSelectedTransaction();
      if (newState == UserChatState.WORKING_WITH_GROUP) {
         // remove action selected
         SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
         builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
         this.updateSession(update, s -> s.setSelectedTransaction(null));
         this.transactionService.delete(selectedTransaction.getId());
         builder.text("Перевод удален. Что делаем дальше?");
         sendMessage(absSender, builder);
      }
   }
}
