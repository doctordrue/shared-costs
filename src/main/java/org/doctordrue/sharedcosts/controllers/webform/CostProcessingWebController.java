package org.doctordrue.sharedcosts.controllers.webform;

import java.time.LocalDateTime;
import java.util.List;

import org.doctordrue.sharedcosts.business.model.processing.CostSplitProcessingInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
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
   private CostGroupService costGroupService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private PersonService personService;
   @Autowired
   private CostGroupWebController costGroupWebController;

   @GetMapping
   public ModelAndView view(@RequestParam("group_id") Long groupId, Model model) {
      CostGroup costGroup = this.costGroupService.findById(groupId);
      List<Currency> currencies = this.currencyService.findAll();
      List<Person> persons = this.personService.findAll();
      CostSplitProcessingInputData data = new CostSplitProcessingInputData();
      data.setTimestamp(LocalDateTime.now());
      data.setCostGroupId(costGroup.getId());
      model.addAttribute("data", data);
      model.addAttribute("group", costGroup);
      model.addAttribute("currencies", currencies);
      model.addAttribute("persons", persons);
      return new ModelAndView("/costs/process", model.asMap());
   }

   @PostMapping
   public ModelAndView submitAndReturn(@ModelAttribute CostSplitProcessingInputData data, Model model) {
      this.costProcessingService.processCost(data);
      return this.costGroupWebController.getCostGroupDetails(data.getCostGroupId(), model);
   }
}
