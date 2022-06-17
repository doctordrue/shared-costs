package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * @author Andrey_Barantsev
 * 6/16/2022
 **/
public abstract class BaseEntityKeyboardReplyProcessor<Key, State extends IBotState, Session extends IBotSession<State>, Entity> extends BaseKeyboardReplyProcessor<Key, State, Session> {

   public BaseEntityKeyboardReplyProcessor(SessionWorker<Key, State, Session> sessionWorker, State targetState) {
      super(sessionWorker, targetState);
   }

   @Override
   protected final ReplyKeyboard getKeyboard(Session session) {
      return keyboardFunction().apply(itemsSupplier(session).get());
   }

   protected abstract Function<Collection<Entity>, ReplyKeyboard> keyboardFunction();

   protected abstract Supplier<Collection<Entity>> itemsSupplier(Session session);
}
