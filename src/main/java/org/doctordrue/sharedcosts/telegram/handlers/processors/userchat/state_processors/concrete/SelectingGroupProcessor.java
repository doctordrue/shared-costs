package org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.concrete;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.group.GroupNotFoundException;
import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.state_processors.base.BaseSingleStateUserChatProcessor;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatSessionWorker;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
@Component
public class SelectingGroupProcessor extends BaseSingleStateUserChatProcessor {

   private static final UserChatState TARGET_STATE = UserChatState.WORKING_WITH_GROUP;
   private final GroupService groupService;

   public SelectingGroupProcessor(UserChatSessionWorker sessionWorker, GroupService groupService) {
      super(sessionWorker, TARGET_STATE);
      this.groupService = groupService;
   }

   @Override
   protected String onSuccessMessage(UserChatSession session) {
      return String.format("Группа '%s' выбрана", session.getSelectedGroup().getName());
   }

   @Override
   protected void verifyMessage(Message message, UserChatSession session) throws BaseException {
      String groupName = message.getText();
      List<Group> groups = this.groupService.findByName(groupName);
      if (groups.isEmpty()) {
         throw new GroupNotFoundException(groupName);
      }
      this.updateSession(session, s -> s.setSelectedGroup(groups.get(0)));
   }

}