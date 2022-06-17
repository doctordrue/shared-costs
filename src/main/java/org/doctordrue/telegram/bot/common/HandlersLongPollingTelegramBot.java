package org.doctordrue.telegram.bot.common;

import java.util.HashMap;
import java.util.Map;

import org.doctordrue.telegram.bot.api.handlers.IUpdateHandler;
import org.doctordrue.telegram.bot.common.handlers.DefaultUpdateHandler;
import org.doctordrue.telegram.bot.common.handlers.HandlerType;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 5/18/2022
 **/
public abstract class HandlersLongPollingTelegramBot extends TelegramLongPollingBot {

   private final Map<HandlerType, IUpdateHandler> handlers = new HashMap<>();
   private final IUpdateHandler emptyHandler = new DefaultUpdateHandler();

   public HandlersLongPollingTelegramBot registerHandler(IUpdateHandler handler, HandlerType type) {
      this.handlers.put(type, handler);
      return this;
   }

   private IUpdateHandler getHandler(HandlerType type) {
      return this.handlers.getOrDefault(type, emptyHandler);
   }

   @Override
   public void onUpdateReceived(Update update) {
      if (update.hasCallbackQuery()) {
         // handle callbacks here
         this.getHandler(HandlerType.CALLBACK).processUpdate(this, update);
      } else if (update.hasMessage()) {
         Message message = update.getMessage();
         Chat chat = message.getChat();
         if (chat.isUserChat()) {
            // handle 1-2-1 messages to bot here
            this.getHandler(HandlerType.USER_CHAT_MESSAGE).processUpdate(this, update);
         } else if (chat.isGroupChat()) {
            // handle group chat messages here
            this.getHandler(HandlerType.GROUP_CHAT_MESSAGE).processUpdate(this, update);
         } else if (chat.isSuperGroupChat()) {
            // handle supergroup chat messages here
            this.getHandler(HandlerType.SUPERGROUP_CHAT_MESSAGE).processUpdate(this, update);
         } else if (chat.isChannelChat()) {
            // handle channel chat messages here
            this.getHandler(HandlerType.CHANNEL_CHAT_MESSAGE).processUpdate(this, update);
         } else {
            // Other chat types if appear in future will be handled here
            this.getHandler(HandlerType.DEFAULT).processUpdate(this, update);
         }
      } else {
         // Handle other type of updates here
         this.getHandler(HandlerType.DEFAULT).processUpdate(this, update);
      }
   }

}