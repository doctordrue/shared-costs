package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.business.model.debt_calculation.CostGroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/21/2022
 **/
@Controller
@RequestMapping("/debts")
public class DebtsWebController {

   @Autowired
   private DebtCalculationService debtCalculationService;

   @GetMapping("calculation")
   public ModelAndView calculate(@RequestParam("group_id") Long groupId, Model model) {
      CostGroupBalance balance = this.debtCalculationService.findAllForCostGroup(groupId);
      model.addAttribute("balance", balance);
      return new ModelAndView("debts/calculation", model.asMap());
   }
}