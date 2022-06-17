package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class SelectingCostProcessor extends BaseStaticKeyboardReplyUserChatProcessor<CostAction> {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_COST;
   private final CostService costService;

   public SelectingCostProcessor(UserChatSessionWorker sessionWorker, CostService costService) {
      super(sessionWorker, TARGET_STATE, CostAction.class);
      this.costService = costService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      if (update.hasMessage() && update.getMessage().hasText()) {
         String text = update.getMessage().getText();
         Optional<Cost> maybeCost = this.costService.findByName(text);
         if (maybeCost.isPresent()) {
            this.updateSession(update, s -> s.setSelectedCost(maybeCost.get()));
            return true;
         }
      }
      return false;
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
