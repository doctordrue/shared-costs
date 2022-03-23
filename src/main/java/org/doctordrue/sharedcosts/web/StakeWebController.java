package org.doctordrue.sharedcosts.web;

import org.doctordrue.sharedcosts.business.model.widget.StakeDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.web.StakeDetailsService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Controller
@RequestMapping("/costs/{cost_id}/stakes")
public class StakeWebController {
   @Autowired
   private StakeDetailsService stakeDetailsService;
   @Autowired
   private CostService costService;
   @Autowired
   private CurrencyService currencyService;

   @GetMapping("/{id}")
   public ModelAndView viewStake(@PathVariable("cost_id") Long costId, @PathVariable("id") Long id, Model model) {
      StakeDetails details = this.stakeDetailsService.getStakeDetails(id);
      Cost cost = this.costService.findById(costId);
      Currency currency = this.currencyService.findById(cost.getCurrencyId());
      model.addAttribute("details", details);
      model.addAttribute("cost", cost);
      model.addAttribute("currency", currency);
      return new ModelAndView("costs/stakes/view", model.asMap());
   }
}
