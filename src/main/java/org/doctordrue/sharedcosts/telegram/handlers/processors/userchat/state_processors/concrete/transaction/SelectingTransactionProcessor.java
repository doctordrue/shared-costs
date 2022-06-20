package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.exceptions.transaction.TransactionNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.TransactionAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
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
public class SelectingTransactionProcessor extends BaseStaticKeyboardReplyUserChatProcessor<TransactionAction> {

   private static final Pattern TRANSACTION_SELECT_PATTERN = Pattern.compile("\\[(\\d+)]\\s(\\d+(\\.\\d{1,2})?)\\s(\\D{1,4}):\\s(.+)\\s->\\s(.+)");

   public SelectingTransactionProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.WORKING_WITH_TRANSACTION, TransactionAction.class);
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Matcher matcher = TRANSACTION_SELECT_PATTERN.matcher(text);
         if (matcher.matches()) {
            UserChatSession session = this.getSession(update);
            Long id = Long.parseLong(matcher.group(1));
            Transaction transaction = session.getSelectedGroup().getTransactions().stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst()
                    .orElseThrow(TransactionNotFoundException::new);
            this.updateSession(update, s -> s.setSelectedTransaction(transaction));
            return true;
         }
         throw new TransactionNotFoundException();
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
