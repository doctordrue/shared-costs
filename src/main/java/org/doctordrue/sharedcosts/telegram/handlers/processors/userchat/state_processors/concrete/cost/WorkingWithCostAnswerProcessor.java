package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete.cost;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.ParticipationService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.CostAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardAnswerUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.sharedcosts.telegram.utils.KeyboardGeneratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/9/2022
 **/
@Component
public class WorkingWithCostAnswerProcessor extends BaseStaticKeyboardAnswerUserChatProcessor<CostAction> {

   private final PaymentService paymentService;
   private final ParticipationService participationService;

   public WorkingWithCostAnswerProcessor(UserChatSessionWorker sessionWorker, PaymentService paymentService, ParticipationService participationService) {
      super(sessionWorker, CostAction.class);
      this.paymentService = paymentService;
      this.participationService = participationService;
   }

   @Override
   protected void onStateChange(AbsSender absSender, UserChatState newState, Update update) {
      SendMessage.SendMessageBuilder builder = SendMessage.builder().chatId(update.getMessage().getChatId().toString());
      builder.replyMarkup(KeyboardGeneratorUtils.removeKeyboard()).text(newState.getMessage());
      Cost selectedCost = this.getSession(update).getSelectedCost();
      if (newState.getOnStateKeyboard() != null) {
         builder.replyMarkup(KeyboardGeneratorUtils.generateStaticKeyboard(newState.getOnStateKeyboard())).text("Что будем делать?");
      } else if (newState == UserChatState.SELECTING_PAYMENT) {
         List<Payment> payments = this.paymentService.findAllByCostId(selectedCost.getId());
         builder.replyMarkup(KeyboardGeneratorUtils.selectPaymentKeyboard(payments));
      } else if (newState == UserChatState.SELECTING_PARTICIPATION) {
         List<Participation> participations = this.participationService.findAllByCostId(selectedCost.getId());
         builder.replyMarkup(KeyboardGeneratorUtils.selectParticipationKeyboard(participations));
      }
      sendMessage(absSender, builder);
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {

   }
}
