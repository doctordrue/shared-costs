package org.doctordrue.sharedcosts.business.model.debt_calculation;

import java.util.StringJoiner;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
public class Debt extends Money {

   private Person debtor;
   private Person creditor;

   public Person getDebtor() {
      return debtor;
   }

   public Debt setDebtor(Person debtor) {
      this.debtor = debtor;
      return this;
   }

   public Person getCreditor() {
      return creditor;
   }

   public Debt setCreditor(Person creditor) {
      this.creditor = creditor;
      return this;
   }

   public Debt setCurrency(Currency currency) {
      super.setCurrency(currency);
      return this;
   }

   public Debt setAmount(Double amount) {
      super.setAmount(amount);
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Debt debtDto = (Debt) o;

      if (!getDebtor().equals(debtDto.getDebtor()))
         return false;
      if (!getCreditor().equals(debtDto.getCreditor()))
         return false;
      if (!getCurrency().equals(debtDto.getCurrency()))
         return false;
      return getAmount().equals(debtDto.getAmount());
   }

   @Override
   public int hashCode() {
      int result = getDebtor().hashCode();
      result = 31 * result + getCreditor().hashCode();
      result = 31 * result + getCurrency().hashCode();
      result = 31 * result + getAmount().hashCode();
      return result;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Debt.class.getSimpleName() + "[", "]")
              .add("debtor=" + this.getDebtor())
              .add("creditor=" + this.getCreditor())
              .add("currency=" + this.getCurrency())
              .add("amount=" + this.getAmount())
              .toString();
   }
}
