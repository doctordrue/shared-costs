package org.doctordrue.sharedcosts.web;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.web.CostGroupDetailsService;
import org.doctordrue.sharedcosts.business.services.web.PersonWebService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jdk.internal.icu.text.NormalizerBase;

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
      model.addAttribute("person", new Person());
      return new ModelAndView("/persons/add", model.asMap());
   }

   @PostMapping("/add/submit")
   public ModelAndView submitAddAndReturn(@ModelAttribute Person person, Model model) {
      this.personService.create(person);
      return this.viewAll(model);
   }

   @PostMapping("/add/new")
   public ModelAndView submitAddAndNew(@ModelAttribute Person person, Model model) {
      this.personService.create(person);
      return this.viewAdd(model);
   }

   @GetMapping("/{id}/update")
   public ModelAndView viewUpdate(@PathVariable("id") Long id, Model model) {
      Person person = this.personService.findById(id);
      model.addAttribute("person", person);
      return new ModelAndView("/persons/edit", model.asMap());
   }

   @PostMapping("/{id}/update")
   public ModelAndView submitEditAndReturn(@PathVariable("id") Long personId, @ModelAttribute Person person, Model model) {
      this.personService.update(personId, person);
      return this.view(personId, model);
   }

   @PostMapping("/{id}/delete")
   public ModelAndView delete(@PathVariable("id") Long id, Model model) {
      this.personService.delete(id);
      return this.viewAll(model);
   }
}
