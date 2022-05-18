package org.doctordrue.sharedcosts.telegram.services;

import java.time.Duration;
import java.util.Random;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.doctordrue.sharedcosts.telegram.data.entities.BotStatus;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramChatSettings;
import org.doctordrue.sharedcosts.telegram.data.repositories.TelegramChatSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrey_Barantsev
 * 5/13/2022
 **/
@Service
public class TelegramChatService {

   @Autowired
   private TelegramChatSettingsRepository telegramChatSettingsRepository;

   public TelegramChatSettings getOrCreate(Long chatId) {
      return this.telegramChatSettingsRepository.findById(chatId)
              .orElseGet(() -> this.telegramChatSettingsRepository.save(TelegramChatSettings.createDefault(chatId)));
   }

   public TelegramChatSettings update(TelegramChatSettings settings) {
      return this.telegramChatSettingsRepository.save(settings);
   }

   public TelegramChatSettings updateBotStatus(Long chatId, @NotNull BotStatus status) {
      TelegramChatSettings persistedSettings = this.getOrCreate(chatId);
      persistedSettings.setBotStatus(status);
      return this.update(persistedSettings);
   }

   @Transactional
   public TelegramChatSettings addStickerSet(Long chatId, String stickerSetName) {
      TelegramChatSettings persistedSettings = this.getOrCreate(chatId);
      persistedSettings.addStickerSet(stickerSetName);
      return this.telegramChatSettingsRepository.save(persistedSettings);
   }

   public String getRandomStickerSetName(Long chatId) {
      final Set<String> stickerSetNames = this.getOrCreate(chatId).getStickerSetNames();
      return stickerSetNames.stream().skip(new Random().nextInt(stickerSetNames.size())).findAny().orElse(null);
   }

   @Transactional
   public Set<String> getStickerSetNames(Long chatId) {
      return this.getOrCreate(chatId).getStickerSetNames();
   }

   public TelegramChatSettings setTimeout(Long chatId, Long timeoutSeconds) {
      TelegramChatSettings settings = this.getOrCreate(chatId);
      settings.setReplyDuration(Duration.ofSeconds(timeoutSeconds).abs());
      return this.telegramChatSettingsRepository.save(settings);
   }

}
