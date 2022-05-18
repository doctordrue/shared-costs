package org.doctordrue.sharedcosts.telegram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.doctordrue.sharedcosts.telegram.bot.UserSession;

/**
 * @author Andrey_Barantsev
 * 5/17/2022
 **/
public class GroupChatSessionHolder {

   private static GroupChatSessionHolder instance;

   private final Map<Long, Set<UserSession>> sessionsToChatMap;

   public void startSession(Long chatId, Long userId) {
      initChatSessionsIfEmpty(chatId);
      UserSession emptySession = new UserSession(userId);
      this.sessionsToChatMap.get(chatId).add(emptySession);
   }

   public void closeSession(Long chatId, Long userId) {
      initChatSessionsIfEmpty(chatId);
      this.sessionsToChatMap.get(chatId).removeIf(s -> Objects.equals(s.getUserId(), userId));
   }

   public UserSession getSession(Long chatId, Long userId) {
      initChatSessionsIfEmpty(chatId);
      return sessionsToChatMap.get(chatId).stream()
              .filter(s -> Objects.equals(s.getUserId(), userId))
              .findFirst()
              .orElse(null);
   }

   private void initChatSessionsIfEmpty(Long chatId) {
      if (!this.sessionsToChatMap.containsKey(chatId)) {
         this.sessionsToChatMap.put(chatId, new HashSet<>());
      }
   }

   private GroupChatSessionHolder() {
      this.sessionsToChatMap = new HashMap<>();
   }

   public static GroupChatSessionHolder getHolder() {
      if (instance == null) {
         instance = new GroupChatSessionHolder();
      }
      return instance;
   }

}
