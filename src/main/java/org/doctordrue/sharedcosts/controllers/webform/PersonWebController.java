package org.doctordrue.sharedcosts.controllers.webform;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.web.PersonWebService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/23/2022
 **/
@Controller
@RequestMapping("/persons")
public class PersonWebController {
   // TODO: use only PersonWebService
   @Autowired
   private PersonService personService;
   @Autowired
   private PersonWebService personWebService;

   @GetMapping
   public ModelAndView viewAll(Model model) {
      List<Person> persons = this.personService.findAll();
      model.addAttribute("persons", persons);
      return new ModelAndView("/persons/index", model.asMap());
   }

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long id, Model model) {
      Person person = this.personService.findById(id);
      List<CostGroup> groups = this.personWebService.findParticipatedGroups(id);
      model.addAttribute("person", person);
      model.addAttribute("groups", groups);
      return new ModelAndView("/persons/view", model.asMap());
   }

   @GetMapping("/add")
   public ModelAndView viewAdd(Model model) {
      Person person = new Person();
      model.addAttribute("person", person);
      model.addAttribute("roles", Stream.of(RoleType.values()).map(RoleType::name).collect(Collectors.toList()));
      return new ModelAndView("/persons/add", model.asMap());
   }

   @GetMapping("/{id}/edit")
   public ModelAndView viewEdit(@PathVariable("id") Long id, Model model) {
      Person person = this.personService.findById(id);
      model.addAttribute("person", person);
      model.addAttribute("roles", Stream.of(RoleType.values()).map(RoleType::name).collect(Collectors.toList()));
      return new ModelAndView("/persons/edit", model.asMap());
   }

   @PostMapping("/add")
   public ModelAndView add(@ModelAttribute("person") @Validated Person person, BindingResult result, Model model) {
      //TODO: move logic to service layer
      String tempPassword = PasswordGeneratorUtil.generate();
      person.setPassword(tempPassword);
      if (result.hasErrors()) {
         model.addAttribute("error", "There are some errors in form, please validate.");
         return this.viewAdd(model);
      }
      if (!personService.register(person)) {
         model.addAttribute("error", "User with e-mail " + person.getEmail() + " already exists. Please change!");
         return this.viewAdd(model);
      }
      Person persistedPerson = this.personService.findByEmail(person.getEmail());
      model.addAttribute("person", persistedPerson);
      model.addAttribute("password", tempPassword);
      return new ModelAndView("/persons/edit", model.asMap());
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long personId, @ModelAttribute Person person, Model model) {
      this.personService.update(personId, person);
      return new RedirectView("/persons");
   }

   @PostMapping("/{id}/reset")
   public ModelAndView resetPassword(@PathVariable("id") Long id, Model model) {
      Person persistedPerson = this.personService.findById(id);
      String password = PasswordGeneratorUtil.generate();
      persistedPerson.setPassword(password);
      this.personService.updatePassword(persistedPerson, password);
      model.addAttribute("person", persistedPerson);
      model.addAttribute("password", password);
      return new ModelAndView("/persons/edit", model.asMap());
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("id") Long id, Model model) {
      this.personService.delete(id);
      return new RedirectView("/persons");
   }
}
