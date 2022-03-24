package org.doctordrue.sharedcosts.controllers.rest.business;

import org.doctordrue.sharedcosts.business.model.debt_calculation.CostGroupBalance;
import org.doctordrue.sharedcosts.business.services.calculation.DebtCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@RestController
@RequestMapping("/api/v1/calculate/debts")
public class DebtController {

   private final DebtCalculationService debtCalculationService;

   public DebtController(DebtCalculationService debtCalculationService) {
      this.debtCalculationService = debtCalculationService;
   }

   @GetMapping(params = {"group_id"})
   public CostGroupBalance findDebtsForGroup(@RequestParam(name = "group_id", required = true) Long groupId) {
      return this.debtCalculationService.findAllForCostGroup(groupId);
   }

}
