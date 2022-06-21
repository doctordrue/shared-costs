package org.doctordrue.sharedcosts.business.services.processing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

   public Participation splitCostParticipation(Cost cost, Set<Person> persons) {
      Participation participation = new Participation().setCost(cost)
              .setName(cost.getName())
              .setAmount(cost.getTotal()).setPeople(persons);
      return this.participationService.create(participation);
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

   public void updateCostTotalFromPayments(Long costId) {
      Cost cost = this.costService.findById(costId);
      Double paymentsTotal = this.paymentService.findAllByCostId(costId).stream()
              .mapToDouble(Payment::getAmount)
              .sum();
      cost.setTotal(paymentsTotal);
      this.costService.update(costId, cost);
   }

   public void updateCostTotalFromParticipation(Long costId) {
      Cost cost = this.costService.findById(costId);
      Double stakesTotal = this.participationService.findAllByCostId(costId).stream()
              .mapToDouble(Participation::getAmount)
              .sum();
      cost.setTotal(stakesTotal);
      this.costService.update(costId, cost);
   }
}
