package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.BaseEntityKeyboardReplyProcessor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 6/16/2022
 **/
public abstract class BaseEntityKeyboardReplyUserChatProcessor<Entity> extends BaseEntityKeyboardReplyProcessor<Chat, UserChatState, UserChatSession, Entity> {

   public BaseEntityKeyboardReplyUserChatProcessor(UserChatSessionWorker sessionWorker, UserChatState targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected Chat getSessionKey(Update update) {
      return update.getMessage().getChat();
   }
}
