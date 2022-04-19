package org.doctordrue.sharedcosts.controllers.webform;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.exceptions.group.BaseGroupServiceException;
import org.doctordrue.sharedcosts.exceptions.group.UnableToDeleteParticipantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@Controller
@RequestMapping("/groups")
public class GroupWebController {

   @Autowired
   private GroupService groupService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private PersonService personService;

   @GetMapping
   public ModelAndView viewAll(Model model) {
      List<Group> groups = this.groupService.findAll();
      Group newGroup = new Group().setStartDate(LocalDate.now());
      model.addAttribute("groups", groups);
      model.addAttribute("new_group", newGroup);
      return new ModelAndView("/groups/index", model.asMap());
   }

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long groupId, Model model) {
      final Group group = this.groupService.findById(groupId);
      final List<Person> candidates = this.groupService.findPeopleToParticipateIn(group);
      final Cost newCost = new Cost().setGroup(group).setDatetime(LocalDateTime.now());
      final List<Currency> currencies = this.currencyService.findAll();
      model.addAttribute("group", group);
      model.addAttribute("new_cost", newCost);
      model.addAttribute("currencies", currencies);
      model.addAttribute("candidates", candidates);
      model.addAttribute("participant", new Person());
      return new ModelAndView("/groups/view", model.asMap());
   }

   @PostMapping("/{id}/participants/add")
   public RedirectView addParticipant(@PathVariable("id") Long id, @ModelAttribute("participant") Person participant) {
      this.groupService.addParticipant(id, participant.getEmail());
      return new RedirectView("/groups/" + id);
   }

   @PostMapping("/{id}/participants/delete")
   public RedirectView deleteParticipant(@PathVariable("id") Long id, @ModelAttribute("participant") Person participant) {
      this.groupService.deleteParticipant(id, participant.getEmail());
      return new RedirectView("/groups/" + id);
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("group") Group group, Principal principal) {
      Person person = this.personService.findByEmail(principal.getName());
      Group persistedGroup = this.groupService.create(group);
      if (person != null) {
         this.addParticipant(persistedGroup.getId(), person);
      }
      return new RedirectView("/groups");
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long id, @ModelAttribute("group") Group group, WebRequest request) {
      this.groupService.update(id, group);
      String referrerUrl = Objects.requireNonNull(request.getHeader(HttpHeaders.REFERER)).toLowerCase();
      if (referrerUrl.contains("/groups/" + id)) {
         return new RedirectView("/groups/" + id);
      }
      return new RedirectView("/groups");
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("id") Long id,
                              @RequestParam(value = "recursively", defaultValue = "false", required = false) Boolean recursively) {
      if (!recursively) {
         this.groupService.delete(id);
      } else {
         this.groupService.deleteRecursively(id);
      }
      return new RedirectView("/groups");
   }

   @ExceptionHandler(BaseGroupServiceException.class)
   public RedirectView handle(RedirectAttributes attributes, BaseGroupServiceException exception) {
      if (exception instanceof UnableToDeleteParticipantException) {
         attributes.addFlashAttribute("participantsError", exception.getLocalizedMessage());
         return new RedirectView("/groups/" + ((UnableToDeleteParticipantException) exception).getGroupId());
      }
      return new RedirectView("/groups");
   }

}
