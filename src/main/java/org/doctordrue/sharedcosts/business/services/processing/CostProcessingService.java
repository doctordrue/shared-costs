package org.doctordrue.sharedcosts.business.services.processing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.processing.CostProcessingResult;
import org.doctordrue.sharedcosts.business.model.processing.CostSplitProcessingInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Stake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@Service
public class CostProcessingService {

   @Autowired
   private CostService costService;
   @Autowired
   private StakeService stakeService;
   @Autowired
   private PaymentService paymentService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private PersonService personService;

   public Cost addCost(String name, CostGroup costGroup, Currency currency, double amount, LocalDateTime timestamp) {
      Cost cost = new Cost();
      cost.setName(name);
      cost.setGroupId(costGroup.getId());
      cost.setCurrencyId(currency.getId());
      cost.setCostTotal(amount);
      cost.setCostDateTime(timestamp);
      return this.costService.create(cost);
   }

   public List<Stake> splitCostStakes(Cost cost, List<Person> persons) {
      double stakeAmount = cost.getCostTotal() / persons.size();
      List<Stake> stakes = persons.stream()
              .map(p -> new Stake()
                      .setCostId(cost.getId())
                      .setPersonId(p.getId())
                      .setStakeTotal(stakeAmount))
              .collect(Collectors.toList());
      return this.stakeService.create(stakes);
   }

   public List<Payment> splitCostPayment(Cost cost,
                                         List<Person> persons) {
      double paymentAmount = cost.getCostTotal() / persons.size();
      List<Payment> payments = persons.stream()
              .map(person -> new Payment()
                      .setPaymentTotal(paymentAmount)
                      .setPersonId(person.getId())
                      .setCostId(cost.getId()))
              .collect(Collectors.toList());
      return this.paymentService.create(payments);
   }

   public CostProcessingResult processCost(String costName,
                                           Long costGroupId,
                                           String currencyShortName,
                                           double amount,
                                           List<Long> stakeholdersIds, List<Long> payersIds,
                                           LocalDateTime timestamp) {
      if (timestamp == null) {
         timestamp = LocalDateTime.now();
      }
      CostGroup costGroup = this.costGroupService.findById(costGroupId);
      Currency currency = this.currencyService.findByShortName(currencyShortName);
      Cost cost = this.addCost(costName, costGroup, currency, amount, timestamp);
      List<Person> stakeholders = this.personService.findByIds(stakeholdersIds);
      List<Person> payers = this.personService.findByIds(payersIds);
      List<Stake> stakes = this.splitCostStakes(cost, stakeholders);
      List<Payment> payments = this.splitCostPayment(cost, payers);
      return new CostProcessingResult().setCost(cost)
              .setStakes(stakes)
              .setPayments(payments);
   }

   public CostProcessingResult processCost(CostSplitProcessingInputData data) {
      return this.processCost(
              data.getName(),
              data.getCostGroupId(),
              data.getCurrencyShortName(),
              data.getAmount(),
              data.getStakeholdersIds(),
              data.getPayersIds(),
              data.getTimestamp());
   }

   public void updateCostTotalFromPayments(Long costId) {
      Cost cost = this.costService.findById(costId);
      Double paymentsTotal = this.paymentService.findAllByCostId(costId).stream()
              .mapToDouble(Payment::getPaymentTotal)
              .sum();
      cost.setCostTotal(paymentsTotal);
      this.costService.update(costId, cost);
   }

   public void updateCostTotalFromStakes(Long costId) {
      Cost cost = this.costService.findById(costId);
      Double stakesTotal = this.stakeService.findAllByCostId(costId).stream()
              .mapToDouble(Stake::getStakeTotal)
              .sum();
      cost.setCostTotal(stakesTotal);
      this.costService.update(costId, cost);
   }
}
