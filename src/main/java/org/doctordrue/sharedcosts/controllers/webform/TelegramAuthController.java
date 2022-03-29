package org.doctordrue.sharedcosts.controllers.webform;

import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/28/2022
 **/
@Controller
@RequestMapping("/auth/telegram")
public class TelegramAuthController {

   @GetMapping
   public ModelAndView loggedInWithTelegram(
           @RequestParam(value = "first_name", required = false) String firstName,
           @RequestParam(value = "last_name", required = false) String lastName,
           @RequestParam(value = "username") String username,
           @RequestParam(value = "photo_url", required = false) URL url,
           @RequestParam(value = "hash") String hash,
           Model model) {
      return new ModelAndView("index", model.asMap());
   }
}
