package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/29/2022
 **/
@Controller
@RequestMapping("/login")
public class LoginController {
   @GetMapping
   public ModelAndView view(Model model) {
      model.addAttribute("person", new Person());
      return new ModelAndView("/login", model.asMap());
   }
}
