package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.business.services.dataaccess.TransactionService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.exceptions.group.ParticipantNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.common.handlers.message.noncommand.processors.exceptions.MessageTypeNotSupportedException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/20/2022
 **/
@Component
public class SelectingNewTransactionFromProcessor extends BaseStaticKeyboardReplyUserChatProcessor<GroupAction> {

   private final TransactionService transactionService;

   public SelectingNewTransactionFromProcessor(UserChatSessionWorker sessionWorker, TransactionService transactionService) {
      super(sessionWorker, UserChatState.WORKING_WITH_GROUP, GroupAction.class);
      this.transactionService = transactionService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         UserChatSession session = this.getSession(update);
         Group group = session.getSelectedGroup();
         Double amount = session.getTempTransactionAmount();
         Person from = group.getParticipants().stream().filter(s -> s.getUsername().equals(text))
                 .findFirst()
                 .orElseThrow(() -> new ParticipantNotFoundException(text, group));
         Person to = group.getParticipants().stream().filter(p -> p.getUsername().equals(update.getMessage().getFrom().getUserName()))
                 .findFirst()
                 .orElseThrow(() -> new ParticipantNotFoundException(text, group));
         Currency currency = session.getCurrency();

         Transaction transaction = new Transaction().setGroup(group)
                 .setAmount(amount)
                 .setCurrency(currency)
                 .setFrom(from)
                 .setTo(to);
         this.transactionService.create(transaction);
         this.updateSession(update, s -> s.setTempTransactionAmount(null).setCurrency(null));
         return true;
      }
      throw new MessageTypeNotSupportedException();
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
