package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.time.LocalDateTime;
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
      return this.paymentRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public List<Payment> findAllByCostId(Long costId) {
      Payment probe = new Payment().setCostId(costId);
      return this.paymentRepository.findAll(Example.of(probe));
   }

   public Payment create(Payment payment) {
      setDateTimeIfNull(payment);
      return this.paymentRepository.save(payment);
   }

   public List<Payment> create(List<Payment> payments) {
      payments.forEach(this::setDateTimeIfNull);
      return this.paymentRepository.saveAll(payments);
   }

   public Payment update(Long id, Payment payment) {
      assumeExists(id);
      payment.setId(id);
      setDateTimeIfNull(payment);
      return this.paymentRepository.save(payment);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.paymentRepository.deleteById(id);
   }

   private void setDateTimeIfNull(Payment payment) {
      if (payment.getPaymentDateTime() == null) {
         payment.setPaymentDateTime(LocalDateTime.now());
      }
   }

   private void assumeExists(Long id) {
      if (!this.paymentRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new BaseException("CS004", "Payment not found for id = " + id);
   }
}