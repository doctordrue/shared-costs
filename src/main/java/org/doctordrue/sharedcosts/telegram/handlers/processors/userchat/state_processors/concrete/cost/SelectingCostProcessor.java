package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.data.entities.Cost;
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
public class SelectingCostProcessor extends BaseSingleStateUserChatProcessor {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final CostService costService;

   public SelectingCostProcessor(UserChatSessionWorker sessionWorker, CostService costService) {
      super(sessionWorker, TARGET_STATE);
      this.costService = costService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return session.getSelectedCost().toTelegramString();
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String text = message.getText();
      Optional<Cost> maybeCost = this.costService.findByName(text);
      if (maybeCost.isPresent()) {
         this.updateSession(session, s -> s.setSelectedCost(maybeCost.get()));
      } else {
         throw new CostNotFoundException(text);
      }
   }
}