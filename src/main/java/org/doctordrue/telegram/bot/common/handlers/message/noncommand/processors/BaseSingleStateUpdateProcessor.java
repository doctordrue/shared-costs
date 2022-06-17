package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.SomethingWentWrongException;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Base class for non-command update processor which moves bot current state to some target state if user input (actually {@link Update} received by bot) applies to some condition
 *
 * @param <Key>   bot session key
 * @param <State> bot state
 */
public abstract class BaseSingleStateUpdateProcessor<Key, State extends IBotState, Session extends IBotSession<State>> extends BaseUpdateProcessor<Key, State, Session> {

   private final State targetState;

   /**
    * @param targetState bot state to move to if <b>expectedUpdatePredicate</b> was successfully tested
    */
   public BaseSingleStateUpdateProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState) {
      super(sessionWorker);
      this.targetState = targetState;
   }

   @Override
   protected final State calculateNewState(Update update) {
      if (this.verifyUpdate(update)) {
         return targetState;
      }
      throw new SomethingWentWrongException(this.getState(update));
   }

   protected State getTargetState() {
      return this.targetState;
   }

   @Override
   protected void onStateChange(AbsSender absSender, State newState, Update update) {
      onExpectedInput(absSender, update);
   }

   /**
    * Defines how bot should react when received input ({@link Update}) that is expected for current bot's state.
    * Usually - this method should send some instructions for user what to do next
    *
    * @param sender {@link AbsSender} to use to send message
    * @param update {@link Update} received on which we want to react
    */
   protected abstract void onExpectedInput(AbsSender sender, Update update);

   /**
    * Defines how to verify user input received is expected
    *
    * @param update {@link Update} received as user input
    * @return <b>true</b> if user input is expected so we need to update bot's state to <b>targetState</b>
    */
   protected abstract boolean verifyUpdate(Update update);
}
