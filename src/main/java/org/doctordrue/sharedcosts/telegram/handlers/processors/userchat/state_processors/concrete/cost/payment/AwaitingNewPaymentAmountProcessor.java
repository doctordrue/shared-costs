package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost.payment;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseEntityKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/16/2022
 **/
@Component
public class AwaitingNewPaymentAmountProcessor extends BaseEntityKeyboardReplyUserChatProcessor<Person> {

   private static final String AMOUNT_REGEX = "^\\d+(\\.\\d{1,2})?$";
   private static final UserChatState TARGET_STATE = UserChatState.NEW_PAYMENT_SELECTING_WHO;

   public AwaitingNewPaymentAmountProcessor(UserChatSessionWorker sessionWorker) {
      super(sessionWorker, TARGET_STATE);
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
            this.updateSession(update, s -> s.setTempPaymentAmount(amount));
            return true;
         }
      }
      return false;
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      sendMessage(sender, SendMessage.builder().chatId(update.getMessage().getChatId().toString()).text("Некорректное значение стоимости, попробуйте еще раз."));
   }
}
