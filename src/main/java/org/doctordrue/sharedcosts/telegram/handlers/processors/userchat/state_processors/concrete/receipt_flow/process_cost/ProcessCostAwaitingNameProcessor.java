package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.receipt_flow.process_cost;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.exceptions.cost.CostNameExistException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/24/2022
 **/
@Component
public class ProcessCostAwaitingNameProcessor extends BaseSingleStateUserChatProcessor {

   private final CostService costService;

   public ProcessCostAwaitingNameProcessor(UserChatSessionWorker sessionWorker, CostService costService) {
      super(sessionWorker, UserChatState.PROCESS_COST_SELECTING_CURRENCY);
      this.costService = costService;
   }

   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Добавляем чек _'%s'_", session.getTempCostName());
   }

   protected void verifyMessage(Message message, UserChatSession session) {
      String text = message.getText();
      LocalDateTime date = LocalDateTime.now();
      String name = text + " " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
      if (StringUtils.isNotBlank(text) && !this.costService.existsByName(name)) {
         this.updateSession(session, s -> s.setTempCostName(name));
      } else {
         throw new CostNameExistException(name);
      }
   }
}
