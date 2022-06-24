package org.doctordrue.telegram.bot.common.handlers.message.noncommand.handler;

import java.util.HashMap;
import java.util.Map;

import org.doctordrue.telegram.bot.api.handlers.IUpdateHandler;
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
public abstract class BaseNonCommandHandler<Key, State extends IBotState<Session>, Session extends IBotSession<State>> extends BaseStateUpdateHandler<Key, State, Session> implements IUpdateHandler {

   private final Map<State, BaseStateUpdateHandler<Key, State, Session>> processorsMap = new HashMap<>();

   public BaseNonCommandHandler(SessionWorker<Key, State, Session> sessionWorker) {
      super(sessionWorker);
   }

   public void registerProcessor(State state, BaseStateUpdateHandler<Key, State, Session> processor) {
      this.processorsMap.put(state, processor);
   }

   @Override
   public void processUpdate(AbsSender absSender, Update update) {

      State state = this.getState(update);
      BaseStateUpdateHandler<Key, State, Session> processor = this.processorsMap.get(state);
      if (processor != null) {
         // state processor has been found. executing
         processor.processUpdate(absSender, update);
      } else {
         // no processors for current state found
         onNoProcessorsFound(absSender, update);
      }
   }

   protected void sendMessage(AbsSender sender, SendMessage.SendMessageBuilder builder) {
      try {
         sender.execute(builder.build());
      } catch (TelegramApiException e) {
         throw new RuntimeException(e);
      }
   }

   protected abstract void onNoProcessorsFound(AbsSender absSender, Update update);

}
