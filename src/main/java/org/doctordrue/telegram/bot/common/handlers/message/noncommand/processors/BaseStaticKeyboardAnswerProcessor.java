package org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors;

import java.util.Arrays;
import java.util.Optional;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;
import org.doctordrue.telegram.bot.api.keyboards.exceptions.OptionNotFoundException;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.doctordrue.telegram.bot.api.session.IBotState;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 6/10/2022
 **/
public abstract class BaseStaticKeyboardAnswerProcessor<Key, State extends IBotState<Session>, Session extends IBotSession<State>, Option extends Enum<? extends KeyboardOption<UserChatState>>>
        extends BaseUpdateProcessor<Key, State, Session> {

   private final Class<Option> answerOnKeyboardEnum;

   public BaseStaticKeyboardAnswerProcessor(SessionWorker<Key, State, Session> sessionWorker, Class<Option> answerOnKeyboardEnum) {
      super(sessionWorker);
      this.answerOnKeyboardEnum = answerOnKeyboardEnum;
   }

   @SuppressWarnings("unchecked")
   @Override
   protected State calculateNewState(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Optional<? extends KeyboardOption<State>> option = Arrays.stream(answerOnKeyboardEnum.getEnumConstants())
                 .map(e -> (KeyboardOption<State>) e)
                 .filter(o -> o.getOption().equals(text))
                 .findFirst();
         if (option.isPresent()) {
            return option.get().getTargetState();
         }
         throw new OptionNotFoundException(text, this.getState(update));
      }
      throw new MessageTypeNotSupportedException();
   }
}
