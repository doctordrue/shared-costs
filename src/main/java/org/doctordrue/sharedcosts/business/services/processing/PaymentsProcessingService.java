package org.doctordrue.sharedcosts.business.services.processing;

import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrey_Barantsev
 * 3/31/2022
 **/
@Service
public class PaymentsProcessingService {

   @Autowired
   private PaymentService paymentService;
   @Autowired
   private CostProcessingService costProcessingService;

   @Transactional
   public Payment processNew(Payment payment, boolean isUpdateCost) {
      Payment persistedPayment = this.paymentService.create(payment);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromPayments(persistedPayment.getCost().getId());
      }
      return persistedPayment;
   }

   @Transactional
   public Payment processEdit(Payment payment, boolean isUpdateCost) {
      Payment persistedPayment = this.paymentService.update(payment.getId(), payment);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromPayments(persistedPayment.getCost().getId());
      }
      return persistedPayment;
   }

   @Transactional
   public void processDelete(Long id, boolean isUpdateCost) {
      Payment persistedPayment = this.paymentService.findById(id);
      this.paymentService.delete(id);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromPayments(persistedPayment.getCost().getId());
      }
   }
}
