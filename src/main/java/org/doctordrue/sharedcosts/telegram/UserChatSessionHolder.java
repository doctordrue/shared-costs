package org.doctordrue.sharedcosts.telegram;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.doctordrue.sharedcosts.telegram.bot.UserSession;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class UserChatSessionHolder {

   private static UserChatSessionHolder instance;
   private final Set<UserSession> sessions;

   private UserChatSessionHolder() {
      this.sessions = new HashSet<>();
   }

   public void startSession(User user) {
      this.sessions.add(UserSession.newSession(user));
   }

   public UserSession getSession(User user) {
      return this.sessions.stream().filter(s -> Objects.equals(s.getUserId(), user.getId()))
              .findFirst()
              .orElse(null);
   }

   public void removeSession(User user) {
      this.sessions.removeIf(s -> Objects.equals(user.getId(), s.getUserId()));
   }

   public static UserChatSessionHolder getHolder() {
      if (instance == null) {
         instance = new UserChatSessionHolder();
      }
      return instance;
   }

}
