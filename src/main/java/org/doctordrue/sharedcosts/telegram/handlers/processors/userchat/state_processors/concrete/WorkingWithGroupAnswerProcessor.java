package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete;

import org.doctordrue.sharedcosts.business.model.debt_calculation.GroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
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
 * 6/8/2022
 **/
@Component
public class WorkingWithGroupAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<GroupAction> {
   private final DebtCalculationService debtCalculationService;

   public WorkingWithGroupAnswerProcessor(UserChatSessionWorker sessionWorker, DebtCalculationService debtCalculationService) {
      super(sessionWorker, GroupAction.class);
      this.debtCalculationService = debtCalculationService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      if (newState == UserChatState.WORKING_WITH_GROUP) {
         SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
         builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
         GroupBalance balance = this.debtCalculationService.calculateGroupBalance(this.getSession(update).getSelectedGroup().getId());
         builder.parseMode(ParseMode.MARKDOWN);
         builder.text(balance.toTelegramString());
         sendMessage(absSender, builder);
      }
   }
}