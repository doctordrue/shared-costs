package org.doctordrue.sharedcosts.business.model.debt_calculation;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.IOwnedAmount;
import org.doctordrue.sharedcosts.data.entities.Person;

public class Total extends Money implements IOwnedAmount {

   private Person person;

   public Person getPerson() {
      return person;
   }

   public Total setPerson(Person person) {
      this.person = person;
      return this;
   }

    @Override
    public Total setAmount(Double amount) {
       super.setAmount(amount);
       return this;
    }

   @Override
   public Total setCurrency(Currency currency) {
      super.setCurrency(currency);
      return this;
   }

   public Total increase(Double amount) {
      if (amount == null) {
         return this;
      }
      double current = this.getAmount() == null ? 0d : this.getAmount();
      this.setAmount(current + amount);
      return this;
   }

   public static Total of(IOwnedAmount amount) {
      return new Total().setAmount(amount.getAmount())
              .setCurrency(amount.getCurrency())
              .setPerson(amount.getPerson());
   }
}
