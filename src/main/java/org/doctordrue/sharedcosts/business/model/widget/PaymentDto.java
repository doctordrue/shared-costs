package org.doctordrue.sharedcosts.business.model.widget;

import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class PaymentDto {

   private Long id;
   private Person person;
   private Double amount;

   public Long getId() {
      return id;
   }

   public PaymentDto setId(Long id) {
      this.id = id;
      return this;
   }

   public Person getPerson() {
      return person;
   }

   public PaymentDto setPerson(Person person) {
      this.person = person;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public PaymentDto setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public static PaymentDto from(Payment payment, Person person) {
      return new PaymentDto().setId(payment.getId())
              .setAmount(payment.getPaymentTotal())
              .setPerson(person);
   }

}
