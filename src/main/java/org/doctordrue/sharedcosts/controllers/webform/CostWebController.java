package org.doctordrue.sharedcosts.controllers.webform;

import java.util.List;

import org.doctordrue.sharedcosts.business.model.widget.CostViewDto;
import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.doctordrue.sharedcosts.business.services.web.CostDetailsService;
import org.doctordrue.sharedcosts.business.services.web.CostViewService;
import org.doctordrue.sharedcosts.business.services.web.PersonWebService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Stake;
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
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Controller
@RequestMapping("/costs")
public class CostWebController {

   @Autowired
   private CostViewService costViewService;
   @Autowired
   private CostService costService;
   @Autowired
   private PaymentService paymentService;
   @Autowired
   private StakeService stakeService;
   @Autowired
   private PersonService personService;
   @Autowired
   private PersonWebService personWebService;

   @Autowired
   private CostProcessingService costProcessingService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private CostDetailsService costDetailsService;

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long id, Model model) {
      CostViewDto dto = this.costViewService.retrieve(id);
      double stakesLeft = dto.getAmount() - dto.getStakes().stream().mapToDouble(StakeDto::getAmount).sum();

      Payment newPayment = new Payment().setCostId(id);
      Stake newStake = new Stake().setCostId(id).setStakeTotal(stakesLeft);

      model.addAttribute("dto", dto);
      model.addAttribute("new_payment", newPayment);
      model.addAttribute("new_stake", newStake);
      return new ModelAndView("/costs/view", model.asMap());
   }

   @GetMapping("/add")
   public ModelAndView viewAdd(@RequestParam("group_id") Long groupId, Model model) {
      CostGroup group = this.costGroupService.findById(groupId);
      List<Currency> currencies = this.currencyService.findAll();
      model.addAttribute("group", group);
      model.addAttribute("currencies", currencies);
      model.addAttribute("cost", new Cost().setCostTotal(0d));
      return new ModelAndView("/costs/add", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("cost") Cost cost, Model model) {
      //TODO: move logic with 0 total to service layer
      if (cost.getCostTotal() == null) {
         cost.setCostTotal(0d);
      }
      Cost persistedCost = this.costService.create(cost);
      return new RedirectView("/costs/" + persistedCost.getId());
   }

   @PostMapping("/{id}/recalculate")
   public RedirectView recalculate(@PathVariable("id") Long id, @RequestParam("from") String from) {
      switch (from) {
         case "stakes":
            this.costProcessingService.updateCostTotalFromStakes(id);
            break;
         case "payments":
         default:
            this.costProcessingService.updateCostTotalFromPayments(id);
            break;
      }
      return new RedirectView("/costs/" + id);
   }
}
