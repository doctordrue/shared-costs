package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.BaseStaticKeyboardAnswerProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/10/2022
 **/
public abstract class BaseStaticKeyboardAnswerUserChatProcessor<Keyboard extends Enum<? extends KeyboardOption<UserChatState>>> extends BaseStaticKeyboardAnswerProcessor<Chat, UserChatState, UserChatSession, Keyboard> {

   public BaseStaticKeyboardAnswerUserChatProcessor(UserChatSessionWorker sessionWorker,
                                                    Class<Keyboard> answerOnKeyboardEnum) {
      super(sessionWorker, answerOnKeyboardEnum);
   }

   @Override
   protected Chat getSessionKey(Update update) {
      return update.getMessage().getChat();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      sendMessage(sender, SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Выберите одно из действий или отправьте команду /stop для отмены"));
   }

}
