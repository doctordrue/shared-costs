package org.doctordrue.sharedcosts.telegram.bot.processors.other;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

import org.doctordrue.sharedcosts.telegram.data.entities.TelegramChatSettings;
import org.doctordrue.sharedcosts.telegram.services.StickerPackService;
import org.doctordrue.sharedcosts.telegram.services.TelegramChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author Andrey_Barantsev
 * 5/13/2022
 **/
@Component
public class StickerReplyProcessor implements INonCommandUpdateProcessor {

   private static final Temporal BEGINNING_OF_TIME = Instant.EPOCH;
   private final Map<Long, Temporal> lastReplyTimestampsMap = new HashMap<>();

   @Autowired
   private StickerPackService stickerPackService;
   @Autowired
   private TelegramChatService telegramChatService;

   @Transactional
   @Override
   public void execute(AbsSender absSender, Update update) {
      if (update.hasMessage()) {
         Message message = update.getMessage();
         long chatId = message.getChatId();
         TelegramChatSettings settings = this.telegramChatService.getOrCreate(chatId);
         Temporal then = this.lastReplyTimestampsMap.getOrDefault(chatId, BEGINNING_OF_TIME);
         Temporal now = Instant.now();
         Duration duration = Duration.between(then, now);
         if (settings.getReplyDuration().compareTo(duration) < 0) {

            this.lastReplyTimestampsMap.put(chatId, now);
            // timeout exceed - bot can reply
            String stickerSetName = this.telegramChatService.getRandomStickerSetName(chatId);
            try {
               Sticker sticker = this.stickerPackService.getRandomSticker(absSender, stickerSetName);
               this.stickerPackService.sendSticker(absSender, chatId, sticker);
            } catch (TelegramApiException | InterruptedException e) {
               throw new RuntimeException(e);
            }
         }

      }
   }

}
