package org.doctordrue.sharedcosts.controllers.webform;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.people.PeopleSelfService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.doctordrue.sharedcosts.utils.PasswordGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/23/2022
 **/
@Controller
@RequestMapping("/persons")
public class PersonWebController {

   @Autowired
   private PersonService personService;
   @Autowired
   private PeopleSelfService peopleSelfService;

   @GetMapping
   @PostMapping
   public ModelAndView viewAll(Model model) {
      List<Person> persons = this.personService.findAll();
      model.addAttribute("persons", persons);
      model.addAttribute("new_person", new Person().setEnabled(true).setLocked(false));
      model.addAttribute("roles", Stream.of(RoleType.values()).map(RoleType::name).collect(Collectors.toList()));
      return new ModelAndView("/persons/index", model.asMap());
   }

   @GetMapping("/{id}/edit")
   @PostMapping("/{id}/edit")
   public ModelAndView viewEdit(@PathVariable("id") Long id, Model model) {
      Person person = this.personService.findById(id);
      model.addAttribute("person", person);
      model.addAttribute("roles", Stream.of(RoleType.values()).map(RoleType::name).collect(Collectors.toList()));
      return new ModelAndView("/persons/edit", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("person") @Validated Person person,
                           BindingResult result,
                           RedirectAttributes model) {
      if (result.hasErrors()) {
         model.addFlashAttribute("error", "There are some errors in form, please validate.");
         return new RedirectView("/persons");
      }
      //TODO: move logic to service layer
      String tempPassword = PasswordGeneratorUtil.generate();
      person.setPassword(tempPassword);
      if (!this.peopleSelfService.register(person)) {
         model.addFlashAttribute("error", "User with e-mail " + person.getUsername() + " already exists. Please change!");
         return new RedirectView("/persons");
      }
      model.addFlashAttribute("message", String.format("User '%s' added. Password generated: %s", person.getUsername(), tempPassword));
      return new RedirectView("/persons");
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long personId, @ModelAttribute Person person, Model model) {
      this.personService.update(personId, person);
      return new RedirectView("/persons");
   }

   @PostMapping("/{id}/reset")
   public RedirectView resetPassword(@PathVariable("id") Long id, RedirectAttributes model) {
      Person persistedPerson = this.personService.findById(id);
      String password = PasswordGeneratorUtil.generate();
      persistedPerson.setPassword(password);
      this.personService.updatePassword(persistedPerson, password);
      model.addFlashAttribute("person", persistedPerson);
      model.addFlashAttribute("password", password);
      return new RedirectView("/persons/" + id + "/edit");
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("id") Long id, RedirectAttributes attributes) {
      this.personService.delete(id);
      attributes.addFlashAttribute("message", "Successfully deleted!");
      return new RedirectView("/persons");
   }
}
