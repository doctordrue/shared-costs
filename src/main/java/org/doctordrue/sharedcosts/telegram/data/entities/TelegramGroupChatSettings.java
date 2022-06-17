package org.doctordrue.sharedcosts.telegram.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.data.entities.Group;

@Entity
@Table(name = "telegram_group_chat_settings")
public class TelegramGroupChatSettings {

   @Id
   @Column(name = "chat_id", nullable = false)
   private Long chatId;

   @OneToOne
   @JoinColumn(name = "group_id")
   private Group group;

   public Long getChatId() {
      return chatId;
   }

   public TelegramGroupChatSettings setChatId(Long chatId) {
      this.chatId = chatId;
      return this;
   }

   public Group getGroup() {
      return group;
   }

   public TelegramGroupChatSettings setGroup(Group group) {
      this.group = group;
      return this;
   }

   public boolean hasGroupAssociated() {
      return this.group != null;
   }

   public static TelegramGroupChatSettings createDefault(Long chatId) {
      return new TelegramGroupChatSettings().setChatId(chatId);
   }
}