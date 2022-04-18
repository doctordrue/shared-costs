package org.doctordrue.sharedcosts.controllers.webform;

import java.security.Principal;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.webform.people.PasswordUpdateInputData;
import org.doctordrue.sharedcosts.business.model.webform.people.SelfEditInputData;
import org.doctordrue.sharedcosts.business.services.people.PeopleSelfService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.exceptions.people.BasePasswordException;
import org.doctordrue.sharedcosts.exceptions.people.BasePersonServiceException;
import org.doctordrue.sharedcosts.exceptions.people.PersonAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
@Controller
@RequestMapping("/self")
public class SelfEditWebController {

   @Autowired
   private PeopleSelfService peopleSelfService;

   @GetMapping
   public ModelAndView viewSelf(Principal principal, Model model) {
      Person person = this.peopleSelfService.load(principal.getName());
      model.addAttribute("person", person);
      model.addAttribute("data", new SelfEditInputData());
      model.addAttribute("password", new PasswordUpdateInputData());
      return new ModelAndView("/self/view", model.asMap());
   }

   @PostMapping
   public RedirectView editSelf(@ModelAttribute("person") SelfEditInputData data,
                                Principal principal,
                                BindingResult result,
                                RedirectAttributes attributes) {
      if (result.hasErrors()) {
         String errors = result.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(", "));
         attributes.addFlashAttribute("updateError", errors);
         return new RedirectView("/self");
      }
      this.peopleSelfService.update(principal.getName(), data);
      return new RedirectView("/self");
   }

   @PostMapping("/password")
   public RedirectView passwordUpdate(@ModelAttribute("password") PasswordUpdateInputData password,
                                      Principal principal,
                                      BindingResult result,
                                      RedirectAttributes attributes) {
      if (result.hasErrors()) {
         String errors = result.getAllErrors().stream().map(ObjectError::toString).collect(Collectors.joining(", "));
         attributes.addFlashAttribute("updateError", errors);
         return new RedirectView("/self");
      }
      this.peopleSelfService.passwordUpdate(principal.getName(), password);
      return new RedirectView("/self");
   }

   @ExceptionHandler(BasePersonServiceException.class)
   public RedirectView handle(RedirectAttributes attributes, BasePersonServiceException exception) {
      if (exception instanceof PersonAlreadyExistsException) {
         attributes.addFlashAttribute("updateError", exception.getMessage());
      }
      if (exception instanceof BasePasswordException) {
         attributes.addFlashAttribute("passwordError", exception.getMessage());
      }
      return new RedirectView("/self");
   }

}
