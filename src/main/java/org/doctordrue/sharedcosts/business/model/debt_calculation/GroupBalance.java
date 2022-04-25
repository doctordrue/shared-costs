package org.doctordrue.sharedcosts.business.model.debt_calculation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class GroupBalance implements Serializable {

   private Group group;
   private final Map<Currency, Double> costTotals = new HashMap<>();
   private final Map<Currency, Double> paymentsBalance = new HashMap<>();
   private final Map<Currency, Double> participationBalance = new HashMap<>();
   private List<Debt> debts = new ArrayList<>();

   public Group getCostGroup() {
      return group;
   }

   public GroupBalance setCostGroup(Group group) {
      this.group = group;
      return this;
   }

   public List<Debt> getDebts() {
      return debts;
   }

   public GroupBalance setDebts(List<Debt> debts) {
      this.debts = debts;
      return this;
   }

   public Set<Money> getPaymentsBalance() {
      return this.toMoneySet(paymentsBalance);
   }

   private Set<Money> toMoneySet(Map<Currency, Double> map) {
      return map.entrySet().stream()
              .map(e -> new Money().setCurrency(e.getKey()).setAmount(e.getValue()))
              .collect(Collectors.toSet());
   }

   public Set<Money> getParticipationBalance() {
      return this.toMoneySet(participationBalance);
   }

   public Set<Money> getCostTotals() {
      return this.toMoneySet(costTotals);
   }

   public GroupBalance addCost(Currency currency, Double amount) {
      costTotals.merge(currency, amount, Double::sum);
      return this;
   }

   public GroupBalance addDebt(Debt debt) {
      this.debts.add(debt);
      return this;
   }

   public GroupBalance addUnpaid(Double amount, Currency currency) {
      this.paymentsBalance.merge(currency, amount, Double::sum);
      return this;
   }

   public GroupBalance addUnallocated(Double amount, Currency currency) {
      this.participationBalance.merge(currency, amount, Double::sum);
      return this;
   }
}
