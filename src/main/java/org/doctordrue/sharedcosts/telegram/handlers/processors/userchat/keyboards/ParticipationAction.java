package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/21/2022
 **/
public enum ParticipationAction implements KeyboardOption<UserChatState> {
   VIEW("Посмотреть информацию о позиции", UserChatState.WORKING_WITH_PARTICIPATION),
   REMOVE("Удалить позицию", UserChatState.WORKING_WITH_COST);
   private final String option;
   private final UserChatState targetState;

   ParticipationAction(String option, UserChatState targetState) {
      this.option = option;
      this.targetState = targetState;
   }

   @Override
   public String getOption() {
      return this.option;
   }

   @Override
   public UserChatState getTargetState() {
      return this.targetState;
   }
}
