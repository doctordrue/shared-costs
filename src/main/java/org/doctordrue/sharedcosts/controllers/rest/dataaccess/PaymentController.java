package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

   private final PaymentService paymentService;

   public PaymentController(PaymentService paymentService) {
      this.paymentService = paymentService;
   }

   @GetMapping
   public List<Payment> findAll(){
      return this.paymentService.findAll();
   }

   @GetMapping(params = "cost_id")
   public List<Payment> findAll(@RequestParam("cost_id") Long costId){
      return this.paymentService.findAllByCostId(costId);
   }

   @GetMapping("/{id}")
   public Payment findById(@PathVariable("id") Long id) {
      return this.paymentService.findById(id);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Payment create(@RequestBody Payment payment) {
      return this.paymentService.create(payment);
   }

   @PutMapping("/{id}")
   public Payment update(@PathVariable("id") Long id, @RequestBody Payment payment) {
      return this.paymentService.update(id, payment);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("id") Long id) {
      this.paymentService.delete(id);
   }
}
