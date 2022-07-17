package org.doctordrue.sharedcosts.telegram.services;

import java.util.Optional;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.telegram.data.entities.TelegramGroupChatSettings;
import org.doctordrue.sharedcosts.telegram.data.repositories.TelegramChatSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 5/13/2022
 **/
@Service
@Profile("telegram")
public class TelegramChatService {
   @Autowired
   private GroupService groupService;
   @Autowired
   private TelegramChatSettingsRepository telegramChatSettingsRepository;

   public TelegramGroupChatSettings getOrCreate(Long chatId) {
      return this.telegramChatSettingsRepository.findById(chatId)
              .orElseGet(() -> this.telegramChatSettingsRepository.save(TelegramGroupChatSettings.createDefault(chatId)));
   }

   public Optional<TelegramGroupChatSettings> findByChatId(Long chatId) {
      return this.telegramChatSettingsRepository.findById(chatId);
   }

   public Optional<TelegramGroupChatSettings> findByGroupId(Long groupId) {
      return this.telegramChatSettingsRepository.findByGroup_Id(groupId);
   }

   public TelegramGroupChatSettings update(TelegramGroupChatSettings settings) {
      return this.telegramChatSettingsRepository.save(settings);
   }

   public void remove(Long chatId) {
      this.telegramChatSettingsRepository.deleteById(chatId);
   }

}
