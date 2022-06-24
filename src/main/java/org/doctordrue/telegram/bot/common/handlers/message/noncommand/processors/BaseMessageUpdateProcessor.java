package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/24/2022
 **/
public abstract class BaseMessageUpdateProcessor<Key, State extends IBotState<Session>, Session extends IBotSession<State>> extends BaseSingleStateUpdateProcessor<Key, State, Session> {

   public BaseMessageUpdateProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      // nothing to do as handled by BaseException's
   }

   @Override
   protected final void onExpectedInput(AbsSender absSender, Update update) {
      Session session = this.getSession(update);
      String messageText = onSuccessMessage(session);
      String chatId = update.getMessage().getChatId().toString();
      if (StringUtils.isNotBlank(messageText)) {
         sendMessage(absSender, SendMessage.builder().chatId(chatId)
                 .parseMode(ParseMode.MARKDOWN)
                 .text(messageText)
                 .build());
      }
   }

   @Override
   protected final boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         Message message = update.getMessage();
         verifyMessage(message, this.getSession(update));
         return true;
      }
      throw new MessageTypeNotSupportedException();
   }

   /**
    * Defines the message bot will send into the chat if answer from user was expected and bot chages his state to targetState
    *
    * @param session current bot's session
    * @return message text as {@link String}
    */
   protected abstract String onSuccessMessage(Session session);

   /**
    * Verifies message received from user. If it is expected & should move bot to targetState then method successfully exit, else - instance of {@link BaseException} should be thrown
    *
    * @param message receive {@link Message} to verify
    * @param session current bot session
    * @throws BaseException in case of non-expected {@link Message} from user received
    */
   protected abstract void verifyMessage(Message message, Session session) throws BaseException;
}
