package org.doctordrue.sharedcosts.business.model;

import java.util.ArrayList;
import java.util.List;

import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class CostGroupBalance {
   private CostGroup costGroup;
   private List<Money> excessPayments;
   private List<Money> excessStakes;
   private List<Debt> debts;

   public CostGroup getCostGroup() {
      return costGroup;
   }

   public CostGroupBalance setCostGroup(CostGroup costGroup) {
      this.costGroup = costGroup;
      return this;
   }

   public List<Debt> getDebts() {
      return debts;
   }

   public CostGroupBalance setDebts(List<Debt> debts) {
      this.debts = debts;
      return this;
   }

   public List<Money> getExcessPayments() {
      return excessPayments;
   }

   public CostGroupBalance setExcessPayments(List<Money> excessPayments) {
      this.excessPayments = excessPayments;
      return this;
   }

   public List<Money> getExcessStakes() {
      return excessStakes;
   }

   public CostGroupBalance setExcessStakes(List<Money> excessStakes) {
      this.excessStakes = excessStakes;
      return this;
   }

   public CostGroupBalance addDebt(Debt debt) {
      if (this.debts == null) {
         this.debts = new ArrayList<>();
      }
      this.debts.add(debt);
      return this;
   }

   public CostGroupBalance addExcessPayment(Double amount, Currency currency) {
      if (this.excessPayments == null) {
         this.excessPayments = new ArrayList<>();
      }
      this.excessPayments.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }

   public CostGroupBalance addExcessStake(Double amount, Currency currency) {
      if (this.excessStakes == null) {
         this.excessStakes = new ArrayList<>();
      }
      this.excessStakes.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }
}
