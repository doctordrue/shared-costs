package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.CostDetails;
import org.doctordrue.sharedcosts.business.model.widget.PaymentDetails;
import org.doctordrue.sharedcosts.business.model.widget.StakeDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Service
public class CostDetailsService {
   @Autowired
   private CostService costService;
   @Autowired
   private PaymentService paymentService;
   @Autowired
   private StakeService stakeService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private PersonService personService;
   @Autowired
   private StakeDetailsService stakeDetailsService;

   public CostDetails getDetails(Cost cost) {
      Currency currency = this.currencyService.findById(cost.getCurrencyId());
      // TODO: add batching for find persons
      List<StakeDetails> stakes = this.stakeDetailsService.getStakesDetails(cost.getId());
      List<PaymentDetails> payments = this.paymentService.findAllByCostId(cost.getId()).stream()
              .map(payment -> new PaymentDetails()
                      .setId(payment.getId())
                      .setAmount(payment.getPaymentTotal())
                      .setTimestamp(payment.getPaymentDateTime()).setPerson(this.personService.findById(payment.getPersonId())))
              .collect(Collectors.toList());
      return new CostDetails()
              .setId(cost.getId())
              .setName(cost.getName())
              .setAmount(cost.getCostTotal())
              .setTimestamp(cost.getCostDateTime())
              .setCurrency(currency)
              .setStakes(stakes)
              .setPayments(payments);
   }
}
