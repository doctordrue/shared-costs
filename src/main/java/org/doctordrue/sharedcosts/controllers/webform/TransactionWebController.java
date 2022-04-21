package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.business.services.dataaccess.TransactionService;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 4/21/2022
 **/
@Controller
@RequestMapping("/groups/{group_id}/transactions")
public class TransactionWebController {

   @Autowired
   private TransactionService transactionService;

   @PostMapping("/add")
   public RedirectView add(@PathVariable("group_id") Long groupId,
                           @ModelAttribute("transaction") Transaction transaction) {
      this.transactionService.create(transaction);
      return new RedirectView("/groups/" + groupId);
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("group_id") Long groupId,
                            @PathVariable("id") Long transactionId,
                            @ModelAttribute("transaction") Transaction transaction) {
      this.transactionService.update(transactionId, transaction);
      return new RedirectView("/groups/" + groupId);
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("group_id") Long groupId,
                              @PathVariable("id") Long transactionId) {
      this.transactionService.delete(transactionId);
      return new RedirectView("/groups/" + groupId);
   }
}