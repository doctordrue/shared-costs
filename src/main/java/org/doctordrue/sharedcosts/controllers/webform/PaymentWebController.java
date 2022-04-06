package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.business.services.processing.PaymentsProcessingService;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/31/2022
 **/
@Controller
@RequestMapping("/costs/{cost_id}/payments")
public class PaymentWebController {
   @Autowired
   private PaymentsProcessingService paymentsProcessingService;

   @PostMapping("/add")
   public RedirectView add(@PathVariable("cost_id") Long costId,
                           @RequestParam(value = "recalculate_cost", required = false, defaultValue = "true") Boolean updateCost,
                           @ModelAttribute Payment payment) {
      this.paymentsProcessingService.processNew(payment, updateCost);
      return new RedirectView("/costs/" + costId);
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("cost_id") Long costId,
                            @PathVariable("id") Long id,
                            @RequestParam(value = "recalculate_cost", required = false, defaultValue = "true") Boolean updateCost,
                            @ModelAttribute Payment payment) {
      this.paymentsProcessingService.processEdit(payment, updateCost);
      return new RedirectView("/costs/" + costId);
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("cost_id") Long costId,
                              @PathVariable("id") Long id,
                              @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") Boolean updateCost) {
      this.paymentsProcessingService.processDelete(id, updateCost);
      return new RedirectView("/costs/" + costId);
   }

}
