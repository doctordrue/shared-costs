package org.doctordrue.sharedcosts.telegram.bot.processors;

import java.util.LinkedList;
import java.util.List;

import org.doctordrue.sharedcosts.telegram.bot.processors.callback.CallbackProcessor;
import org.doctordrue.sharedcosts.telegram.bot.processors.other.INonCommandUpdateProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Andrey_Barantsev
 * 5/11/2022
 **/
@Component
public class NonCommandProcessor {

   private final List<INonCommandUpdateProcessor> processors = new LinkedList<>();
   private CallbackProcessor callbackProcessor;

   public NonCommandProcessor register(INonCommandUpdateProcessor processor) {
      this.processors.add(processor);
      return this;
   }

   public NonCommandProcessor registerCallbackProcessor(CallbackProcessor callbackProcessor) {
      this.callbackProcessor = callbackProcessor;
      return this;
   }

   public void execute(AbsSender absSender, Update update) {
      if (update.hasCallbackQuery()) {
         CallbackQuery callbackQuery = update.getCallbackQuery();
         if (this.callbackProcessor != null) {
            this.callbackProcessor.execute(absSender, callbackQuery);
         }
         // here is callback from inline keyboard
      } else {
         processors.forEach(p -> p.execute(absSender, update));
      }
   }

}
