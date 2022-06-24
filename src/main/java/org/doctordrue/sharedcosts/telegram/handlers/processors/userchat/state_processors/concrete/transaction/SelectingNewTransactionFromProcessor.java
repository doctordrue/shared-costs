package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.transaction;

import org.doctordrue.sharedcosts.business.services.dataaccess.TransactionService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.group.ParticipantNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/20/2022
 **/
@Component
public class SelectingNewTransactionFromProcessor extends BaseSingleStateUserChatProcessor {

   private final TransactionService transactionService;

   public SelectingNewTransactionFromProcessor(UserChatSessionWorker sessionWorker, TransactionService transactionService) {
      super(sessionWorker, UserChatState.WORKING_WITH_GROUP);
      this.transactionService = transactionService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      String template = "Перевод в группе '%s' успешно создан";
      return String.format(template, session.getSelectedGroup().getName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Group group = session.getSelectedGroup();
      Double amount = session.getTempTransactionAmount();
      Person from = group.getParticipants().stream().filter(s -> s.getUsername().equals(text))
              .findFirst()
              .orElseThrow(() -> new ParticipantNotFoundException(text, group));
      Person to = group.getParticipants().stream().filter(p -> p.getUsername().equals(message.getFrom().getUserName()))
              .findFirst()
              .orElseThrow(() -> new ParticipantNotFoundException(text, group));
      Currency currency = session.getCurrency();

      Transaction transaction = new Transaction().setGroup(group)
              .setAmount(amount)
              .setCurrency(currency)
              .setFrom(from)
              .setTo(to);
      this.transactionService.create(transaction);
      this.updateSession(session, s -> s.setTempTransactionAmount(null).setCurrency(null));
   }
}