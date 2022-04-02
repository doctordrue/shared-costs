package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.PaymentDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/31/2022
 **/
@Service
public class PaymentDetailsService {

   @Autowired
   private PaymentService paymentService;
   @Autowired
   private PersonService personService;
   @Autowired
   private CostService costService;
   @Autowired
   private CurrencyService currencyService;

   public PaymentDto findDetails(Long id) {
      Payment payment = this.paymentService.findById(id);
      return convertFromPayment(payment);
   }

   public List<PaymentDto> findAllDetails(Cost cost) {
      return this.paymentService.findAllByCostId(cost.getId()).stream()
              .map(this::convertFromPayment)
              .collect(Collectors.toList());
   }

   private PaymentDto convertFromPayment(Payment payment) {
      //TODO: refactor to reduce calls to DB
      Person person = this.personService.findById(payment.getPersonId());
      return new PaymentDto()
              .setId(payment.getId())
              .setAmount(payment.getPaymentTotal())
              .setPerson(person);
   }
}
