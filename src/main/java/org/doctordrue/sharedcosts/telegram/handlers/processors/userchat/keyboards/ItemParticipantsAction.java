package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards;

import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.keyboards.KeyboardOption;

/**
 * @author Andrey_Barantsev
 * 6/23/2022
 **/
public enum ItemParticipantsAction implements KeyboardOption<UserChatState> {
    ADD_MORE("Добавить участника", UserChatState.ALLOCATE_ITEMS_SELECTING_PARTICIPANT),
    FINISH("К следующей позиции в чеке", UserChatState.ALLOCATE_ITEMS_SELECTING_ITEM);

    ItemParticipantsAction(String option, UserChatState targetState) {
        this.option = option;
        this.targetState = targetState;
    }

    private final String option;
    private final UserChatState targetState;

    @Override
   public String getOption() {
      return this.option;
   }

   @Override
   public UserChatState getTargetState() {
      return this.targetState;
   }
}
