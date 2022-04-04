package org.doctordrue.sharedcosts.controllers.webform;

import java.time.LocalDateTime;
import java.util.List;

import org.doctordrue.sharedcosts.business.model.processing.CostSplitProcessingInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/23/2022
 **/
@Controller
@RequestMapping("/costs/process")
public class CostProcessingWebController {

   @Autowired
   private CostProcessingService costProcessingService;
   @Autowired
   private GroupService groupService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private PersonService personService;

   @GetMapping
   public ModelAndView viewProcess(@RequestParam("group_id") Long groupId, Model model) {
      Group group = this.groupService.findById(groupId);
      List<Currency> currencies = this.currencyService.findAll();
      List<Person> persons = this.personService.findAll();
      CostSplitProcessingInputData data = new CostSplitProcessingInputData();
      data.setTimestamp(LocalDateTime.now());
      data.setCostGroupId(group.getId());
      model.addAttribute("data", data);
      model.addAttribute("group", group);
      model.addAttribute("currencies", currencies);
      model.addAttribute("persons", persons);
      return new ModelAndView("/costs/process", model.asMap());
   }

   @PostMapping
   public RedirectView submitProcess(@ModelAttribute CostSplitProcessingInputData data, Model model) {
      this.costProcessingService.processCost(data);
      return new RedirectView("/groups/" + data.getCostGroupId() + "/details");
   }
}
