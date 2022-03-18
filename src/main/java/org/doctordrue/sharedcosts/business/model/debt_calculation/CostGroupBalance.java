package org.doctordrue.sharedcosts.business.model.debt_calculation;

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
   private List<Money> paymentsBalance;
   private List<Money> stakesBalance;
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

   public List<Money> getPaymentsBalance() {
      return paymentsBalance;
   }

   public CostGroupBalance setPaymentsBalance(List<Money> paymentsBalance) {
      this.paymentsBalance = paymentsBalance;
      return this;
   }

   public List<Money> getStakesBalance() {
      return stakesBalance;
   }

   public CostGroupBalance setStakesBalance(List<Money> stakesBalance) {
      this.stakesBalance = stakesBalance;
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
      if (this.paymentsBalance == null) {
         this.paymentsBalance = new ArrayList<>();
      }
      this.paymentsBalance.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }

   public CostGroupBalance addExcessStake(Double amount, Currency currency) {
      if (this.stakesBalance == null) {
         this.stakesBalance = new ArrayList<>();
      }
      this.stakesBalance.add(new Money().setAmount(amount).setCurrency(currency));
      return this;
   }
}
