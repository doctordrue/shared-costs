package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardAnswerUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class WorkingWithCostAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<CostAction> {

   private final CostService costService;

   public WorkingWithCostAnswerProcessor(UserChatSessionWorker sessionWorker, CostService costService) {
      super(sessionWorker, CostAction.class);
      this.costService = costService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      Cost selectedCost = this.getSession(update).getSelectedCost();
      if (newState == UserChatState.WORKING_WITH_COST) {
         // cost info action
         SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
         builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
         builder.parseMode(ParseMode.MARKDOWN).text(selectedCost.toTelegramString());
         sendMessage(absSender, builder);
      } else if (newState == UserChatState.WORKING_WITH_GROUP) {
         // cost remove action
         this.updateSession(update, s -> s.setSelectedCost(null));
         this.costService.delete(selectedCost.getId());
      }
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
