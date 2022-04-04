package org.doctordrue.sharedcosts.business.services.processing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.processing.CostProcessingResult;
import org.doctordrue.sharedcosts.business.model.processing.CostSplitProcessingInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.ParticipationService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
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
   private ParticipationService participationService;
   @Autowired
   private PaymentService paymentService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private GroupService groupService;
   @Autowired
   private PersonService personService;

   public Cost addCost(String name, Group group, Currency currency, double amount, LocalDateTime timestamp) {
      Cost cost = new Cost();
      cost.setName(name);
      cost.setGroup(group);
      cost.setCurrency(currency);
      cost.setTotal(amount);
      cost.setDatetime(timestamp);
      return this.costService.create(cost);
   }

   public List<Participation> splitCostParticipation(Cost cost, List<Person> persons) {
      double participationAmount = cost.getTotal() / persons.size();
      List<Participation> participations = persons.stream()
              .map(person -> new Participation()
                      .setName(String.format("1/%s of %s", persons.size(), cost.getName()))
                      .setCost(cost)
                      .setPerson(person)
                      .setAmount(participationAmount))
              .collect(Collectors.toList());
      return this.participationService.create(participations);
   }

   public List<Payment> splitCostPayment(Cost cost,
                                         List<Person> persons) {
      double paymentAmount = cost.getTotal() / persons.size();
      List<Payment> payments = persons.stream()
              .map(person -> new Payment()
                      .setName(String.format("1/%s for %s", persons.size(), cost.getName()))
                      .setAmount(paymentAmount)
                      .setPerson(person)
                      .setCost(cost))
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
      Group group = this.groupService.findById(costGroupId);
      Currency currency = this.currencyService.findByShortName(currencyShortName);
      Cost cost = this.addCost(costName, group, currency, amount, timestamp);
      List<Person> stakeholders = this.personService.findByIds(stakeholdersIds);
      List<Person> payers = this.personService.findByIds(payersIds);
      List<Participation> participations = this.splitCostParticipation(cost, stakeholders);
      List<Payment> payments = this.splitCostPayment(cost, payers);
      return new CostProcessingResult().setCost(cost)
              .setStakes(participations)
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
              .mapToDouble(Payment::getAmount)
              .sum();
      cost.setTotal(paymentsTotal);
      this.costService.update(costId, cost);
   }

   public void updateCostTotalFromStakes(Long costId) {
      Cost cost = this.costService.findById(costId);
      Double stakesTotal = this.participationService.findAllByCostId(costId).stream()
              .mapToDouble(Participation::getAmount)
              .sum();
      cost.setTotal(stakesTotal);
      this.costService.update(costId, cost);
   }
}
