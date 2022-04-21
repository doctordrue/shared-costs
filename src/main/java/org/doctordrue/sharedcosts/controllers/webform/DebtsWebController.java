package org.doctordrue.sharedcosts.controllers.webform;

import java.util.List;

import org.doctordrue.sharedcosts.business.model.debt_calculation.GroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.doctordrue.sharedcosts.data.entities.IOwnedAmount;
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
      GroupBalance balance = this.debtCalculationService.calculateGroupBalance(groupId);
      List<IOwnedAmount> participationTotals = this.debtCalculationService.findParticipationTotal(groupId);
      List<IOwnedAmount> paymentTotals = this.debtCalculationService.findPaymentTotal(groupId);
      model.addAttribute("balance", balance);
      model.addAttribute("participation_totals", participationTotals);
      model.addAttribute("payment_totals", paymentTotals);
      return new ModelAndView("debts/calculation", model.asMap());
   }
}