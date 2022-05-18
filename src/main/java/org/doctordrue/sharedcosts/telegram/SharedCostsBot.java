package org.doctordrue.sharedcosts.telegram;

import java.util.Collections;
import java.util.List;

import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.doctordrue.sharedcosts.telegram.bot.processors.NonCommandProcessor;
import org.doctordrue.sharedcosts.telegram.bot.processors.other.INonCommandUpdateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Andrey_Barantsev
 * 5/11/2022
 **/
public class SharedCostsBot extends TelegramLongPollingCommandBot {

   @Value("${telegram.bot.name}")
   private String botUserName;
   @Value("${telegram.bot.token}")
   private String botToken;
   @Value("${app.admin.username}")
   private String adminUserName;
   @Value("${app.admin.password}")
   private String adminPassword;
   @Autowired
   private PasswordEncoder encoder;

   private final NonCommandProcessor nonCommandProcessor;

   public SharedCostsBot(DefaultBotOptions options, NonCommandProcessor nonCommandProcessor) {
      super(options);
      this.nonCommandProcessor = nonCommandProcessor;
   }

   public void registerNonCommandProcessor(INonCommandUpdateProcessor processor) {
      this.nonCommandProcessor.register(processor);
   }

   @Override
   public String getBotUsername() {
      return this.botUserName;
   }

   public void processNonCommandUpdate(Update update) {
      this.nonCommandProcessor.execute(this, update);
   }

   @Override
   public void onUpdatesReceived(List<Update> updates) {
      authenticate();
      super.onUpdatesReceived(updates);
   }

   @Override
   public String getBotToken() {
      return this.botToken;
   }

   public void authenticate() {
      if (SecurityContextHolder.getContext().getAuthentication() == null || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
         Authentication auth = new UsernamePasswordAuthenticationToken(adminUserName, this.encoder.encode(adminPassword), Collections.singletonList(RoleType.ADMIN));
         SecurityContextHolder.getContext().setAuthentication(auth);
      }
   }

}
