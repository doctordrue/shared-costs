package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.BaseNoKeyboardReplyProcessor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 6/10/2022
 **/
public abstract class BaseNoKeyboardReplyUserChatProcessor extends BaseNoKeyboardReplyProcessor<Chat, UserChatState, UserChatSession> {

   /**
    * @param sessionWorker
    * @param targetState   bot state to move to if <b>expectedUpdatePredicate</b> was successfully tested
    */
   public BaseNoKeyboardReplyUserChatProcessor(UserChatSessionWorker sessionWorker, UserChatState targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected Chat getSessionKey(Update update) {
      return update.getMessage().getChat();
   }
}
