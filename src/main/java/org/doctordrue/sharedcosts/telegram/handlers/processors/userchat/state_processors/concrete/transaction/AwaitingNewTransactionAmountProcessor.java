package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.parse.MoneyFormatException;
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
public class AwaitingNewTransactionAmountProcessor extends BaseSingleStateUserChatProcessor {

   private static final String AMOUNT_REGEX = "^\\d+(\\.\\d{1,2})?$";

   public AwaitingNewTransactionAmountProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.NEW_TRANSACTION_SELECTING_FROM);
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Добавляем перевод на %.2f %s";
      return String.format(template, session.getTempTransactionAmount(), session.getCurrency().getShortName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      if (text.matches(AMOUNT_REGEX)) {
         Double amount = Double.parseDouble(text);
         this.updateSession(session, s -> s.setTempTransactionAmount(amount));
      } else {
         throw new MoneyFormatException();
      }
   }
}
