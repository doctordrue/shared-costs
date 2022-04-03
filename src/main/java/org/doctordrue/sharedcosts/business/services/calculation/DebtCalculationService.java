package org.doctordrue.sharedcosts.business.services.calculation;

import org.doctordrue.sharedcosts.business.model.debt_calculation.CostGroupBalance;
import org.doctordrue.sharedcosts.business.model.debt_calculation.Debt;
import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;
import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.*;
import org.doctordrue.sharedcosts.business.services.web.CostGroupDetailsService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class DebtCalculationService {

   private final CostGroupService costGroupService;
   private final CostService costService;
   private final PaymentService paymentService;
   private final StakeService stakeService;
   private final CurrencyService currencyService;
   private final PersonService personService;
   private final CostGroupDetailsService costGroupDetailsService;

   public DebtCalculationService(CostGroupService costGroupService,
                                 CostService costService,
                                 PaymentService paymentService,
                                 StakeService stakeService,
                                 CurrencyService currencyService,
                                 PersonService personService, CostGroupDetailsService costGroupDetailsService) {
      this.costGroupService = costGroupService;
      this.costService = costService;
      this.paymentService = paymentService;
      this.stakeService = stakeService;
      this.currencyService = currencyService;
      this.personService = personService;
      this.costGroupDetailsService = costGroupDetailsService;
   }

   public List<Total> findStakesTotal(Long groupId) {
      final CostGroup costGroup = this.costGroupService.findById(groupId);
      final Currency currency = this.currencyService.findById(1L);
      return this.costGroupDetailsService.getDetails(costGroup).getCosts()
              .stream()
              .flatMap(costDetails -> costDetails.getStakes().stream())
              .collect(Collectors.toMap(StakeDto::getPerson, StakeDto::getAmount, Double::sum))
              .entrySet().stream().map(entry -> new Total().setPerson(entry.getKey()).setAmount(entry.getValue()).setCurrency(currency))
              .collect(Collectors.toList());
   }

   public CostGroupBalance findAllForCostGroup(Long groupId) {
      final Map<Long, Currency> currencyMap = this.currencyService.findAll().stream().collect(Collectors.toMap(Currency::getId, v -> v));
      final CostGroup costGroup = this.costGroupService.findById(groupId);
      final List<Cost> allCosts = this.costService.findAllByGroupId(groupId);

      CostGroupBalance result = new CostGroupBalance();
      result.setCostGroup(costGroup);

      Map<Currency, List<Cost>> costsByCurrency = allCosts.stream().collect(Collectors.groupingBy(c -> currencyMap.get(c.getCurrencyId())));
      System.out.println("Costs:");
      costsByCurrency.forEach((currency, costs) -> {
         // calculate debts for each currency separately
         double totalCost = costs.stream().mapToDouble(Cost::getCostTotal).sum();
         System.out.println(currency.getShortName() + ": " + totalCost);

         List<Payment> payments = costs.stream()
                 .flatMap(cost -> this.paymentService.findAllByCostId(cost.getId()).stream())
                 .collect(Collectors.toList());
         final double totalPayments = payments.stream().mapToDouble(Payment::getPaymentTotal).sum();
         System.out.println("Payments total: " + totalPayments);
         if (totalPayments != totalCost) {
            result.addExcessPayment(totalPayments - totalCost, currency);
         }
         List<Stake> stakes = costs.stream()
                 .flatMap(cost -> this.stakeService.findAllByCostId(cost.getId()).stream())
                 .collect(Collectors.toList());
         final double totalStakes = stakes.stream().mapToDouble(Stake::getStakeTotal).sum();
         System.out.println("Stakes total: " + totalStakes);
         if (totalCost != totalStakes) {
            result.addExcessStake(totalStakes - totalCost, currency);
         }

         // find creditors credits & debtors debts
         Map<Long, Double> paymentsMap = payments.stream().collect(Collectors.toMap(Payment::getPersonId, Payment::getPaymentTotal, Double::sum));
         Map<Long, Double> stakesMap = stakes.stream().collect(Collectors.toMap(Stake::getPersonId, Stake::getStakeTotal, Double::sum));
         Set<Long> personIds = new HashSet<>(paymentsMap.keySet());
         personIds.addAll(stakesMap.keySet());
         final List<Person> persons = this.personService.findByIds(personIds);

         Map<Person, Double> creditorsMap = new HashMap<>();
         Map<Person, Double> debtorsMap = new HashMap<>();
         for (Person person : persons) {
            final double credit = paymentsMap.getOrDefault(person.getId(), 0d);
            final double debt = stakesMap.getOrDefault(person.getId(), 0d);
            final double balance = credit - debt;
            if (balance > 0) {
               creditorsMap.put(person, balance);
            } else {
               debtorsMap.put(person, balance);
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
