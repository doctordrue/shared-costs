package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.repositories.PaymentRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class PaymentService {

   private final PaymentRepository paymentRepository;

   public PaymentService(PaymentRepository paymentRepository) {
      this.paymentRepository = paymentRepository;
   }

   public List<Payment> findAll() {
      return this.paymentRepository.findAll();
   }

   public Payment findById(Long id) {
      return this.paymentRepository.findById(id).orElseThrow(() -> new BaseException("CS004", "Payment not found for id = " + id));
   }

   public List<Payment> findAllByCostId(Long costId) {
      Payment probe = new Payment().setCostId(costId);
      return this.paymentRepository.findAll(Example.of(probe));
   }
}
