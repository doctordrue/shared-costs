package org.doctordrue.sharedcosts.web;

import java.util.List;

import org.doctordrue.sharedcosts.business.model.widget.CostDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.web.CostDetailsService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Controller
@RequestMapping("/costs")
public class CostWebController {
   @Autowired
   private CostGroupWebController costGroupsController;
   @Autowired
   private CostService costService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private CostDetailsService costDetailsService;

   @GetMapping("/add")
   public ModelAndView addCostFormView(@RequestParam("group_id") Long groupId, Model model) {
      CostGroup group = this.costGroupService.findById(groupId);
      List<Currency> currencies = this.currencyService.findAll();
      model.addAttribute("group", group);
      model.addAttribute("currencies", currencies);
      model.addAttribute("cost", new Cost());
      return new ModelAndView("/costs/add", model.asMap());
   }

   @PostMapping
   public ModelAndView addCostFormSubmit(@ModelAttribute("cost") Cost cost, Model model) {
      this.costService.create(cost);
      return this.costGroupsController.getCostGroupDetails(cost.getGroupId(), model);
   }

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long id, Model model) {
      Cost cost = this.costService.findById(id);
      CostDetails costDetails = this.costDetailsService.getDetails(cost);
      model.addAttribute("details", costDetails);
      return new ModelAndView("/costs/view", model.asMap());
   }
}
