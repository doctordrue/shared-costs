package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/29/2022
 **/
@Controller
@RequestMapping("/register")
public class RegisterController {
   @Autowired
   private PersonService personService;

   @GetMapping
   public ModelAndView viewRegister(Model model) {
      model.addAttribute("person", new Person());
      return new ModelAndView("/register", model.asMap());
   }

   @PostMapping
   public ModelAndView register(@ModelAttribute("person") @Validated Person person, BindingResult result, Model model) {
      if (result.hasErrors()) {
         model.addAttribute("error", "There are some errors in form, please validate.");
         return this.viewRegister(model);
      }
      if (!personService.register(person)){
         model.addAttribute("error", "User with e-mail " + person.getEmail() + " already exists. Please change!");
         return this.viewRegister(model);
      }
      return new ModelAndView(new RedirectView("/login"), model.asMap());
   }
}
