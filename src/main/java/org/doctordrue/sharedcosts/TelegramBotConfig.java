package org.doctordrue.sharedcosts;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.telegram.SharedCostsBot;
import org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat.AddMeCommand;
import org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat.DebtsCommand;
import org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat.InitCommand;
import org.doctordrue.sharedcosts.telegram.handlers.commands.groupchat.RemoveCommand;
import org.doctordrue.sharedcosts.telegram.handlers.commands.userchat.StartCommand;
import org.doctordrue.sharedcosts.telegram.handlers.commands.userchat.StopCommand;
import org.doctordrue.sharedcosts.telegram.handlers.processors.userchat.UserChatNonCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
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
   private AddMeCommand addMeCommand;
   @Autowired
   private InitCommand initCommand;
   @Autowired
   private DebtsCommand debtsCommand;
   @Autowired
   private RemoveCommand removeCommand;
   @Autowired
   private StartCommand startCommand;
   @Autowired
   private StopCommand stopCommand;
   @Autowired
   private UserChatNonCommandHandler handler;
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
      SharedCostsBot bot = new SharedCostsBot(defaultBotOptions());

      /* Costs related commands */
      bot.register(this.initCommand);
      bot.register(this.addMeCommand);
      bot.register(this.debtsCommand);
      bot.register(this.removeCommand);

      /* User chat commands */
      bot.register(this.startCommand);
      bot.register(this.stopCommand);
      bot.register(this.handler);

      return bot;
   }
}