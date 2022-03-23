package org.doctordrue.sharedcosts.web;

import java.util.List;

import org.doctordrue.sharedcosts.business.model.widget.CostGroupDetails;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.web.CostGroupDetailsService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@Controller
@RequestMapping("/groups")
public class CostGroupWebController {

   private final CostGroupService costGroupService;
   private final DebtCalculationService debtCalculationService;
   private final CostGroupDetailsService costGroupDetailsService;

   public CostGroupWebController(CostGroupService costGroupService, DebtCalculationService debtCalculationService, CostGroupDetailsService costGroupDetailsService) {
      this.costGroupService = costGroupService;
      this.debtCalculationService = debtCalculationService;
      this.costGroupDetailsService = costGroupDetailsService;
   }

   @GetMapping
   public ModelAndView getAllCostGroups(Model model){
      List<CostGroup> groups = this.costGroupService.findAll();
      model.addAttribute("groups", groups);
      return new ModelAndView("/groups/index", model.asMap());
   }

   @GetMapping("/{id}")
   public ModelAndView viewCostGroup(@PathVariable("id") Long id, Model model) {
      CostGroup group = this.costGroupService.findById(id);
      model.addAttribute("group", group);
      return new ModelAndView("/groups/edit", model.asMap());
   }

   @GetMapping("/{id}/details")
   public ModelAndView getCostGroupDetails(@PathVariable("id") Long groupId, Model model) {
      CostGroup group = this.costGroupService.findById(groupId);
      CostGroupDetails groupDetails = this.costGroupDetailsService.getDetails(group);
      model.addAttribute("costGroupDetails", groupDetails);
      return new ModelAndView("/groups/view", model.asMap());
   }

   @PostMapping("/edit/{id}")
   public ModelAndView updateCostGroup(@PathVariable("id") Long id, @ModelAttribute("group") CostGroup group, Model model) {
      CostGroup updated = this.costGroupService.update(id, group);
      model.addAttribute("group", updated);
      return this.getCostGroupDetails(id, model);
   }

   @PostMapping("/delete/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public ModelAndView deleteCostGroup(@PathVariable("id") Long id, Model model) {
      this.costGroupService.delete(id);
      return this.getAllCostGroups(model);
   }

}
