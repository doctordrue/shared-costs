package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.deprecated;

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
 * 6/16/2022
 **/
@Component
@Deprecated
public class AwaitingNewPaymentAmountProcessor extends BaseSingleStateUserChatProcessor {

   private static final String AMOUNT_REGEX = "^\\d+(\\.\\d{1,2})?$";
   private static final UserChatState TARGET_STATE = UserChatState.NEW_PAYMENT_SELECTING_WHO;

   public AwaitingNewPaymentAmountProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, TARGET_STATE);
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Создаем оплату чека '%s' на сумму %.2f %s";
      return String.format(template, session.getSelectedCost().getName(), session.getTempPaymentAmount(), session.getCurrency());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      if (text.matches(AMOUNT_REGEX)) {
         double amount = Double.parseDouble(text);
         this.updateSession(session, s -> s.setTempPaymentAmount(amount));
      } else {
         throw new MoneyFormatException();
      }
   }
}
