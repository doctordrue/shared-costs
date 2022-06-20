package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.participation;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.exceptions.parse.MoneyFormatException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseEntityKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
@Component
public class AwaitingNewParticipationPriceProcessor extends BaseEntityKeyboardReplyUserChatProcessor<Person> {

   private static final String AMOUNT_REGEX = "^\\d+(\\.\\d{1,2})?$";

   public AwaitingNewParticipationPriceProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, UserChatState.NEW_PARTICIPATION_AWAITING_WHO);
   }

   @Override
   protected Function<Collection<Person>, ReplyKeyboard> keyboardFunction() {
      return KeyboardGeneratorUtils::selectPersonKeyboard;
   }

   @Override
   protected Supplier<Collection<Person>> itemsSupplier(UserChatSession session) {
      return session.getSelectedGroup()::getParticipants;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         if (text.matches(AMOUNT_REGEX)) {
            double amount = Double.parseDouble(text);
            this.updateSession(update, s -> s.setTempParticipationAmount(amount));
            return true;
         }
         throw new MoneyFormatException();
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
