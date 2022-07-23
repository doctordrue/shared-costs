package org.doctordrue.sharedcosts.telegram.services;

import org.doctordrue.sharedcosts.telegram.data.entities.UserChatSession;
import org.doctordrue.sharedcosts.telegram.data.repositories.UserChatSessionRepository;
import org.doctordrue.telegram.bot.api.session.ISessionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

/**
 * @author Andrey_Barantsev
 * 5/24/2022
 **/
@Service
public class UserChatSessionService implements ISessionHolder<Chat, UserChatSession> {

   @Autowired
   private UserChatSessionRepository userChatSessionRepository;

   @Override
   public boolean hasSession(Chat key) {
      return this.userChatSessionRepository.existsById(key.getId());
   }

   @Override
   public UserChatSession getSession(Chat key) {
      return this.userChatSessionRepository.findById(key.getId())
              .orElseGet(() -> this.userChatSessionRepository.save(UserChatSession.newSession(key)));
   }

   @Override
   public void persist(UserChatSession session) {
      this.userChatSessionRepository.save(session);
   }

   @Override
   public void removeSession(Chat key) {
      this.userChatSessionRepository.deleteById(key.getId());
   }
}
