package org.doctordrue.sharedcosts.controllers.webform;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.processing.CostProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
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
   private CostService costService;
   @Autowired
   private CostProcessingService costProcessingService;

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long id, Model model) {
      Cost cost = this.costService.findById(id);
      List<Total> participationTotals = cost.getParticipationsTotals().stream()
              .collect(Collectors.toMap(Total::getPerson, Total::getAmount, Double::sum))
              .entrySet().stream()
              .map(e -> new Total().setPerson(e.getKey())
                      .setCurrency(cost.getCurrency())
                      .setAmount(e.getValue()))
              .collect(Collectors.toList());
      double participationLeft = cost.getTotal() - cost.getParticipations().stream()
              .mapToDouble(Participation::getAmount)
              .sum();
      double paymentLeft = cost.getTotal() - cost.getPayments().stream()
              .mapToDouble(Payment::getAmount)
              .sum();

      Payment newPayment = new Payment()
              .setName(String.format("%s payment", cost.getName()))
              .setCost(cost)
              .setAmount(paymentLeft);
      Participation newParticipation = new Participation()
              .setCost(cost)
              .setAmount(participationLeft);

      model.addAttribute("cost", cost);
      model.addAttribute("new_payment", newPayment);
      model.addAttribute("new_participation", newParticipation);
      model.addAttribute("participation_totals", participationTotals);
      return new ModelAndView("/costs/view", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("cost") Cost cost) {
      cost.setDatetime(LocalDateTime.now());
      Cost persistedCost = this.costService.create(cost);
      return new RedirectView("/costs/" + persistedCost.getId());
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long id,
                            @ModelAttribute("cost") Cost cost,
                            WebRequest request) {
      Cost persistedCost = this.costService.findById(id);
      cost.setId(id).setGroup(persistedCost.getGroup());
      this.costService.update(id, cost);
      String referrerUrl = Objects.requireNonNull(request.getHeader(HttpHeaders.REFERER)).toLowerCase();
      if (referrerUrl.contains("/costs/" + id)) {
         return new RedirectView("/costs/" + id);
      }
      return new RedirectView("/groups/" + cost.getGroup().getId());
   }

   @PostMapping("/{id}/delete")
   public RedirectView edit(@PathVariable("id") Long id) {
      Cost persistedCost = this.costService.findById(id);
      this.costService.delete(id);
      return new RedirectView("/groups/" + persistedCost.getGroup().getId());
   }

   @PostMapping("/{id}/recalculate")
   public RedirectView recalculate(@PathVariable("id") Long id, @RequestParam("from") String from) {
      switch (from) {
         case "participation":
            this.costProcessingService.updateCostTotalFromParticipation(id);
            break;
         case "payments":
         default:
            this.costProcessingService.updateCostTotalFromPayments(id);
            break;
      }
      return new RedirectView("/costs/" + id);
   }
}
