package org.doctordrue.sharedcosts.business.services.calculation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.doctordrue.sharedcosts.business.model.debt_calculation.CostGroupBalance;
import org.doctordrue.sharedcosts.business.model.debt_calculation.Debt;
import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;
import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.IOwnedAmount;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class DebtCalculationService {

   @Autowired
   private GroupService groupService;
   @Autowired
   private PersonService personService;

   public List<IOwnedAmount> findParticipationTotal(Long groupId) {
      return this.findTotals(this.groupService.findById(groupId), Cost::getParticipations);
   }

   public List<IOwnedAmount> findPaymentTotal(Long groupId) {
      return this.findTotals(this.groupService.findById(groupId), Cost::getPayments);
   }

   private List<IOwnedAmount> findTotals(Group group, Function<Cost, List<? extends IOwnedAmount>> function) {
      return group.getCosts().stream()
              .flatMap(c -> function.apply(c).stream())
              .map(Total::of)
              .collect(
                      Collectors.groupingBy(Total::getCurrency,
                              Collectors.toMap(Total::getPerson, v -> v, (a, b) -> a.increase(b.getAmount()))))
              .values()
              .stream()
              .flatMap(m -> m.values().stream())
              .collect(Collectors.toList());
   }

   public CostGroupBalance findAllForCostGroup(Long groupId) {
      final Group group = this.groupService.findById(groupId);
      final List<Cost> allCosts = group.getCosts();

      CostGroupBalance result = new CostGroupBalance();
      result.setCostGroup(group);

      Map<Currency, List<Cost>> costsByCurrency = allCosts.stream().collect(Collectors.groupingBy(Cost::getCurrency));
      System.out.println("Costs:");
      costsByCurrency.forEach((currency, costs) -> {
         // calculate debts for each currency separately
         double totalCost = costs.stream().mapToDouble(Cost::getTotal).sum();
         System.out.println(currency.getShortName() + ": " + totalCost);

         List<Payment> payments = costs.stream()
                 .flatMap(cost -> cost.getPayments().stream())
                 .collect(Collectors.toList());
         final double totalPayments = payments.stream().mapToDouble(Payment::getAmount).sum();
         System.out.println("Payment total: " + totalPayments);
         if (totalPayments != totalCost) {
            result.addExcessPayment(totalPayments - totalCost, currency);
         }
         List<Participation> participations = costs.stream()
                 .flatMap(cost -> cost.getParticipations().stream())
                 .collect(Collectors.toList());
         final double totalParticipation = participations.stream().mapToDouble(Participation::getAmount).sum();
         System.out.println("Participation total: " + totalParticipation);
         if (totalCost != totalParticipation) {
            result.addExcessStake(totalParticipation - totalCost, currency);
         }

         // find creditors credits & debtors debts
         Map<Person, Double> paymentsMap = payments.stream().collect(Collectors.toMap(Payment::getPerson, Payment::getAmount, Double::sum));
         Map<Person, Double> participationMap = participations.stream().collect(Collectors.toMap(Participation::getPerson, Participation::getAmount, Double::sum));

         final List<Person> participants = costs.stream()
                 .flatMap(cost -> Stream.concat(cost.getParticipations().stream().map(Participation::getPerson), cost.getPayments().stream().map(Payment::getPerson)))
                 .collect(Collectors.toList());

         Map<Person, Double> creditorsMap = new HashMap<>();
         Map<Person, Double> debtorsMap = new HashMap<>();

         for (Person participant : participants) {
            final double credit = paymentsMap.getOrDefault(participant, 0d);
            final double debt = participationMap.getOrDefault(participant, 0d);
            final double balance = credit - debt;
            if (balance > 0) {
               creditorsMap.put(participant, balance);
            } else {
               debtorsMap.put(participant, balance);
            }
         }

         for (Map.Entry<Person, Double> creditorEntry : creditorsMap.entrySet()) {
            // for each creditor we want to find debtors to fill out the credit
            double creditorBalance = creditorEntry.getValue();
            if (creditorBalance > 0) {
               // creditor still needs to be paid off
               // find debtors to reduce creditor's balance and increase debtors balance
               for (Map.Entry<Person, Double> debtorEntry : debtorsMap.entrySet()) {
                  double debtorBalance = debtorEntry.getValue();
                  if (debtorBalance < 0) {
                     // debtor still have to pay
                     Debt debt = new Debt()
                             .setCreditor(creditorEntry.getKey())
                             .setDebtor(debtorEntry.getKey())
                             .setCurrency(currency);
                     if (creditorBalance + debtorBalance > 0) {
                        //debtor has fully paid off and don't have to pay any more
                        //creditor still needs to receive money from another debtor
                        debt.setAmount(-debtorBalance);
                        creditorBalance += debtorBalance;
                        debtorEntry.setValue(0d);
                     } else {
                        //debtor still owes to cost group
                        //creditor is fully paid
                        debt.setAmount(creditorBalance);
                        debtorEntry.setValue(debtorBalance + creditorBalance);
                        creditorBalance = 0d;
                     }
                     creditorEntry.setValue(creditorBalance);
                     result.addDebt(debt);
                     if (creditorBalance <= 0) {
                        break;
                     }
                  }
               }
            }
         }
      });

      return result;
   }

}
