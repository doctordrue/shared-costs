package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.exceptions.group.GroupNotFoundException;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.keyboards.GroupAction;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseStaticKeyboardReplyUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
@Component
public class SelectingGroupProcessor extends BaseStaticKeyboardReplyUserChatProcessor<GroupAction> {

   private static final String ERROR_GROUP_NOT_FOUND_MESSAGE = "Группа с таким именем не найдена";
   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_GROUP;
   private final GroupService groupService;
   private Group selectedGroup;

   public SelectingGroupProcessor(UserChatSessionWorker sessionWorker, GroupService groupService) {
      super(sessionWorker, TARGET_STATE, GroupAction.class);
      this.groupService = groupService;
   }

   @Override
   protected boolean verifyUpdate(Update update) {
      String groupName = update.getMessage().getText();
      List<Group> groups = this.groupService.findByName(groupName);
      if (groups.isEmpty()) {
         throw new GroupNotFoundException(groupName);
      }
      this.selectedGroup = groups.get(0);
      this.updateSession(update, s -> s.setSelectedGroup(selectedGroup));
      return true;
   }

   @Override
   protected void onNonExpectedInput(AbsSender sender, Update update) {
      Chat chat = update.getMessage().getChat();
      sendMessage(sender, SendMessage.builder().chatId(chat.getId().toString()).text(ERROR_GROUP_NOT_FOUND_MESSAGE));
   }

}
