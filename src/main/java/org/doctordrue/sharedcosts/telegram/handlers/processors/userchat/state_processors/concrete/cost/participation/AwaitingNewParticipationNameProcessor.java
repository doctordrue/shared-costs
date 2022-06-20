package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation;

import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseNoKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
@Component
public class AwaitingNewParticipationNameProcessor extends BaseNoKeyboardReplyUserChatProcessor {

   public AwaitingNewParticipationNameProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.NEW_PARTICIPATION_AWAITING_PRICE);
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         this.updateSession(update, s -> s.setTempParticipationName(text));
         return true;
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
