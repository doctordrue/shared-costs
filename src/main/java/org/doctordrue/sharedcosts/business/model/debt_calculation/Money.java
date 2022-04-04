package org.doctordrue.sharedcosts.business.model.debt_calculation;

import java.io.Serializable;
import java.util.StringJoiner;

import org.doctordrue.sharedcosts.data.entities.Currency;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
public class Money implements Serializable {

   private Currency currency;
   private Double amount;

   public Currency getCurrency() {
      return currency;
   }

   public Money setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public Money setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Money money = (Money) o;

      if (getCurrency() != null ? !getCurrency().equals(money.getCurrency()) : money.getCurrency() != null)
         return false;
      return getAmount() != null ? getAmount().equals(money.getAmount()) : money.getAmount() == null;
   }

   @Override
   public int hashCode() {
      int result = getCurrency() != null ? getCurrency().hashCode() : 0;
      result = 31 * result + (getAmount() != null ? getAmount().hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Money.class.getSimpleName() + "[", "]")
              .add("currency=" + this.getCurrency())
              .add("amount=" + this.getAmount())
              .toString();
   }
}
