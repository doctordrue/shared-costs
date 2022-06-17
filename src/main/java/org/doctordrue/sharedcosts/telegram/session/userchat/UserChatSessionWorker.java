package org.doctordrue.sharedcosts.telegram.session.userchat;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.services.UserChatSessionService;
import org.doctordrue.telegram.bot.api.session.ISessionHolder;
import org.doctordrue.telegram.bot.api.session.SessionWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;

/**
 * @author Andrey_Barantsev
 * 6/8/2022
 **/
@Component
public class UserChatSessionWorker implements SessionWorker<Chat, UserChatState, UserChatSession> {

   @Autowired
   private UserChatSessionService userChatSessionService;

   @Override
   public ISessionHolder<Chat, UserChatSession> getSessionHolder() {
      return this.userChatSessionService;
   }
}