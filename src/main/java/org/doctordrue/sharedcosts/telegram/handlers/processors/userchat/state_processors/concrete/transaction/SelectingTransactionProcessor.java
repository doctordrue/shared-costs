package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.transaction.TransactionNotFoundException;
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
public class SelectingTransactionProcessor extends BaseSingleStateUserChatProcessor {

   private static final Pattern TRANSACTION_SELECT_PATTERN = Pattern.compile("\\[(\\d+)]\\s(\\d+(\\.\\d{1,2})?)\\s(\\D{1,4}):\\s(.+)\\s->\\s(.+)");

   public SelectingTransactionProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.WORKING_WITH_TRANSACTION);
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Работаем с переводом от %s на сумму %.2f %s",
              session.getSelectedTransaction().getFrom().getFullName(),
              session.getSelectedTransaction().getAmount(),
              session.getSelectedTransaction().getCurrency().getShortName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Matcher matcher = TRANSACTION_SELECT_PATTERN.matcher(text);
      if (matcher.matches()) {
         Long id = Long.parseLong(matcher.group(1));
         Transaction transaction = session.getSelectedGroup().getTransactions().stream()
                 .filter(t -> t.getId().equals(id))
                 .findFirst()
                 .orElseThrow(TransactionNotFoundException::new);
         this.updateSession(session, s -> s.setSelectedTransaction(transaction));
      } else {
         throw new TransactionNotFoundException();
      }
   }

}