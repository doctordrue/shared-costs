package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

import org.doctordrue.sharedcosts.business.services.processing.ParticipationProcessingService;
import org.doctordrue.sharedcosts.data.entities.Participation;
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
 * 6/24/2022
 **/
@Component
public class ProcessCostAwaitingItemPriceProcessor extends BaseSingleStateUserChatProcessor {

   private static final String AMOUNT_REGEX = "^\\d+(\\.\\d{1,2})?$";

   private final ParticipationProcessingService participationProcessingService;

   public ProcessCostAwaitingItemPriceProcessor(UserChatSessionWorker sessionWorker, ParticipationProcessingService participationProcessingService) {
      super(sessionWorker, UserChatState.PROCESS_COST_SELECTING_ACTION);
      this.participationProcessingService = participationProcessingService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return null;
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      if (text.matches(AMOUNT_REGEX)) {
         Double amount = Double.parseDouble(text);

         this.participationProcessingService.processNew(new Participation()
                 .setCost(session.getSelectedCost())
                 .setAmount(amount)
                 .setName(session.getTempParticipationName()), true);
         this.updateSession(session, s -> s.setTempParticipationName(null));
      } else {
         throw new MoneyFormatException();
      }
   }
}
