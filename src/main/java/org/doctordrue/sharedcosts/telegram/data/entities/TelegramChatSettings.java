package org.doctordrue.sharedcosts.telegram.data.entities;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.data.entities.Group;

@Entity
@Table(name = "telegram_chat_settings")
public class TelegramChatSettings {

   private static final Long DEFAULT_REPLY_DURATION_SECONDS = 120L;
   private static final String DEFAULT_STICKER_SET_NAME = "MrPepe";

   @Id
   @Column(name = "chat_id", nullable = false)
   private Long chatId;

   @OneToOne(fetch = FetchType.EAGER)
   private Group group;

   @ElementCollection(fetch = FetchType.EAGER)
   private Set<String> stickerSetNames = new HashSet<>();

   @Enumerated(EnumType.STRING)
   @Column(name = "bot_status")
   private BotStatus botStatus = BotStatus.BEFORE_START;

   @Column(name = "reply_duration")
   private Duration replyDuration = Duration.ofSeconds(DEFAULT_REPLY_DURATION_SECONDS);

   public Long getChatId() {
      return chatId;
   }

   public TelegramChatSettings setChatId(Long chatId) {
      this.chatId = chatId;
      return this;
   }

   public Group getGroup() {
      return group;
   }

   public TelegramChatSettings setGroup(Group group) {
      this.group = group;
      return this;
   }

   public Set<String> getStickerSetNames() {
      return stickerSetNames;
   }

   public TelegramChatSettings setStickerSetNames(Set<String> stickerSetNames) {
      this.stickerSetNames = stickerSetNames;
      return this;
   }

   public Duration getReplyDuration() {
      return replyDuration;
   }

   public TelegramChatSettings setReplyDuration(Duration replyDuration) {
      this.replyDuration = replyDuration;
      return this;
   }

   public BotStatus getBotStatus() {
      return botStatus;
   }

   public TelegramChatSettings setBotStatus(BotStatus botStatus) {
      this.botStatus = botStatus;
      return this;
   }

   public TelegramChatSettings addStickerSet(String stickerSetName) {
      this.stickerSetNames.add(stickerSetName);
      return this;
   }

   public boolean hasGroupAssociated() {
      return this.group != null;
   }

   public static TelegramChatSettings createDefault(Long chatId) {
      return new TelegramChatSettings().setChatId(chatId)
              .setReplyDuration(Duration.ofSeconds(DEFAULT_REPLY_DURATION_SECONDS))
              .addStickerSet(DEFAULT_STICKER_SET_NAME)
              .setBotStatus(BotStatus.BEFORE_START);
   }
}