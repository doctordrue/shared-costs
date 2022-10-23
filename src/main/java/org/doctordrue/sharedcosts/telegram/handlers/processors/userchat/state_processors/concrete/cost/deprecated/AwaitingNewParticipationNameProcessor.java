package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.deprecated;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
@Component
@Deprecated
public class AwaitingNewParticipationNameProcessor extends BaseSingleStateUserChatProcessor {

   public AwaitingNewParticipationNameProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.NEW_PARTICIPATION_AWAITING_PRICE);
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Создаем позицию '%s' в чеке '%s'";
      return String.format(template, session.getTempParticipationName(), session.getSelectedCost().getName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      this.updateSession(session, s -> s.setTempParticipationName(text));
   }
}
