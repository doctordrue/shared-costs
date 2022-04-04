package org.doctordrue.sharedcosts.business.model.debt_calculation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class CostGroupBalance implements Serializable {

   private Group group;
   private List<Money> paymentsBalance = new ArrayList<>();
   private List<Money> participationBalance = new ArrayList<>();
   private List<Debt> debts = new ArrayList<>();

   public Group getCostGroup() {
      return group;
   }

   public CostGroupBalance setCostGroup(Group group) {
      this.group = group;
      return this;
   }

   public List<Debt> getDebts() {
      return debts;
   }

   public CostGroupBalance setDebts(List<Debt> debts) {
      this.debts = debts;
      return this;
   }

   public List<Money> getPaymentsBalance() {
      return paymentsBalance;
   }

   public CostGroupBalance setPaymentsBalance(List<Money> paymentsBalance) {
      this.paymentsBalance = paymentsBalance;
      return this;
   }

   public List<Money> getParticipationBalance() {
      return participationBalance;
   }

   public CostGroupBalance setParticipationBalance(List<Money> participationBalance) {
      this.participationBalance = participationBalance;
      return this;
   }

   public CostGroupBalance addDebt(Debt debt) {
      this.debts.add(debt);
      return this;
   }

   public CostGroupBalance addExcessPayment(Double amount, Currency currency) {
      this.paymentsBalance.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }

   public CostGroupBalance addExcessStake(Double amount, Currency currency) {
      this.participationBalance.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }
}
