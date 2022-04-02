package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;

import org.doctordrue.sharedcosts.business.model.widget.CostDetails;
import org.doctordrue.sharedcosts.business.model.widget.PaymentDto;
import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
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
   private PaymentDetailsService paymentDetailsService;
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
      // TODO: add batching to find people
      List<StakeDto> stakes = this.stakeDetailsService.findAllDetails(cost);
      List<PaymentDto> payments = this.paymentDetailsService.findAllDetails(cost);
      return new CostDetails()
              .setGroupId(cost.getGroupId())
              .setId(cost.getId())
              .setName(cost.getName())
              .setAmount(cost.getCostTotal())
              .setTimestamp(cost.getCostDateTime())
              .setCurrency(currency)
              .setStakes(stakes)
              .setPayments(payments);
   }
}
