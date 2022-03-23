package org.doctordrue.sharedcosts.business.services.calculation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.debt_calculation.CostGroupBalance;
import org.doctordrue.sharedcosts.business.model.debt_calculation.Debt;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

   public DebtCalculationService(CostGroupService costGroupService,
                                 CostService costService,
                                 PaymentService paymentService,
                                 StakeService stakeService,
                                 CurrencyService currencyService,
                                 PersonService personService) {
      this.costGroupService = costGroupService;
      this.costService = costService;
      this.paymentService = paymentService;
      this.stakeService = stakeService;
      this.currencyService = currencyService;
      this.personService = personService;
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

//         if (!(CollectionUtils.isEmpty(result.getPaymentsBalance()) && CollectionUtils.isEmpty(result.getStakesBalance()))) {
//            return;
//         }

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
