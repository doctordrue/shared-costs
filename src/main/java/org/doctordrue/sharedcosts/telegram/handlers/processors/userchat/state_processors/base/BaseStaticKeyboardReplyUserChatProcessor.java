package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.BaseStaticKeyboardReplyProcessor;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public abstract class BaseStaticKeyboardReplyUserChatProcessor<Keyboard extends Enum<? extends KeyboardOption<UserChatState>>> extends BaseStaticKeyboardReplyProcessor<Chat, UserChatState, UserChatSession, Keyboard> {

   public BaseStaticKeyboardReplyUserChatProcessor(UserChatSessionWorker sessionWorker, UserChatState targetState, Class<Keyboard> keyboardEnum) {
      super(sessionWorker, targetState, keyboardEnum);
   }

   @Override
   protected Chat getSessionKey(Update update) {
      return update.getMessage().getChat();
   }
}
