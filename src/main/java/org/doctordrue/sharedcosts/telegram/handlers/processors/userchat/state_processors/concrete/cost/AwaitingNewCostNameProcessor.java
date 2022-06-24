package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.cost.CostNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class AwaitingNewCostNameProcessor extends BaseSingleStateUserChatProcessor {

   private static final UserChatState TARGET_STATE = UserChatState.NEW_COST_SELECTING_CURRENCY;
   private final CostService costService;

   public AwaitingNewCostNameProcessor(UserChatSessionWorker sessionWorker, CostService costService) {
      super(sessionWorker, TARGET_STATE);
      this.costService = costService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Создаем чек с названием '%s'", session.getTempCostName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      if (StringUtils.isNotBlank(text) && !this.costService.existsByName(text)) {
         this.updateSession(message.getChat(), s -> s.setTempCostName(text));
      } else {
         throw new CostNotFoundException(text);
      }
   }
}