package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.doctordrue.telegram.bot.common.handlers.BaseStateUpdateHandler;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
public abstract class BaseUpdateProcessor<Key, State extends IBotState, Session extends IBotSession<State>> extends BaseStateUpdateHandler<Key, State, Session> {

   public BaseUpdateProcessor(SessionWorker<Key, State, Session> sessionWorker) {
      super(sessionWorker);
   }

   /**
    * Send simple text message
    *
    * @param sender  {@link AbsSender} to send the message
    * @param builder {@link org.telegram.telegrambots.meta.api.methods.send.SendMessage.SendMessageBuilder} builder of message to send
    */
   protected void sendMessage(AbsSender sender, SendMessage.SendMessageBuilder builder) {
      try {
         sender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public final void processUpdate(AbsSender absSender, Update update) {
      State newState = null;

      // new flow - to manage non-expected states by throwing exceptions
      try {
         newState = calculateNewState(update);
         if (newState != null) {
            this.setState(update, newState);
            onStateChange(absSender, newState, update);
         }
      } catch (BaseException exception) {
         sendMessage(absSender, SendMessage.builder()
                 .chatId(update.getMessage().getChatId().toString())
                 .replyToMessageId(update.getMessage().getMessageId())
                 .text("Произошла ошибка: " + exception.getMessage() + " " + this.getState(update).getMessage()));
      } finally {
         if (newState == null) {
            // to support old flow
            onNonExpectedInput(absSender, update);
         }
      }

   }

   /**
    * Defines how to calculate new bot's state based on the {@link Update} received
    *
    * @param update {@link Update} received
    * @return new bot state
    * @throws BaseException in case new state cannot be calculated e.g. non-expected input was received
    */
   protected abstract State calculateNewState(Update update) throws BaseException;

   /**
    * Defines how bot should react in case of changing its state to <b>newState</b> after receiving the <b>update</b>
    *
    * @param absSender {@link AbsSender} to senf the message to user
    * @param newState  the state bot is moving to
    * @param update    {@link Update} that caused state moving
    */
   protected abstract void onStateChange(AbsSender absSender, State newState, Update update);

   /**
    * Defines how bot should react on non-expected input ({@link Update} received).
    * Usually that should be something like a message that input was not recognized with some details of bot's current state and actions it expects from user
    *
    * @param sender {@link AbsSender} to use to send message
    * @param update {@link Update} received on which we want to react
    */
   protected abstract void onNonExpectedInput(AbsSender sender, Update update);
}
