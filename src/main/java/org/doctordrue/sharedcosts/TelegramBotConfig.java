package org.doctordrue.sharedcosts;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.telegram.SharedCostsBot;
import org.doctordrue.sharedcosts.telegram.bot.commands.AddMeCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.AddStickerPackCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.CancelCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.InitCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.SetTimeoutCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.StartCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.StickerPackCommand;
import org.doctordrue.sharedcosts.telegram.bot.commands.StickerPacksCommand;
import org.doctordrue.sharedcosts.telegram.bot.processors.NonCommandProcessor;
import org.doctordrue.sharedcosts.telegram.bot.processors.other.ReplyToBotMentionProcessor;
import org.doctordrue.sharedcosts.telegram.bot.processors.other.StickerReplyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author Andrey_Barantsev
 * 5/11/2022
 **/
@Configuration
public class TelegramBotConfig {

   @Value("${telegram.api.url}")
   private String url;
   @Autowired
   private StartCommand startCommand;
   @Autowired
   private AddMeCommand addMeCommand;
   @Autowired
   private SetTimeoutCommand setTimeoutCommand;
   @Autowired
   private StickerPacksCommand stickerPacksCommand;
   @Autowired
   private StickerPackCommand stickerPackCommand;
   @Autowired
   private AddStickerPackCommand addStickerPackCommand;
   @Autowired
   private NonCommandProcessor nonCommandProcessor;
   @Autowired
   private StickerReplyProcessor stickerReplyProcessor;
   @Autowired
   private InitCommand initCommand;
   @Autowired
   private CancelCommand cancelCommand;

   @Bean
   public DefaultBotOptions defaultBotOptions() {
      DefaultBotOptions options = new DefaultBotOptions();
      if (StringUtils.isNotEmpty(this.url)) {
         options.setBaseUrl(url);
      }
      return options;
   }

   @Bean
   public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
      return new TelegramBotsApi(DefaultBotSession.class);
   }

   @Bean
   public SharedCostsBot sharedCostsBot() {
      SharedCostsBot bot = new SharedCostsBot(defaultBotOptions(), this.nonCommandProcessor);
      bot.register(this.startCommand);
//      bot.register(this.initCommand);
      bot.register(this.addMeCommand);
      bot.register(this.setTimeoutCommand);
      bot.register(this.stickerPacksCommand);
      bot.register(this.stickerPackCommand);
      bot.register(this.addStickerPackCommand);
      bot.register(this.cancelCommand);
      bot.register(new HelpCommand());
      bot.registerNonCommandProcessor(this.stickerReplyProcessor);
      bot.registerNonCommandProcessor(new ReplyToBotMentionProcessor());
      return bot;
   }
}