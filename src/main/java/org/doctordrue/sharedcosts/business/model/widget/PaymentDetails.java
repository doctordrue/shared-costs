package org.doctordrue.sharedcosts.business.model.widget;

import java.time.LocalDateTime;

import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class PaymentDetails {

   private Long id;
   private Person person;
   private Double amount;
   private LocalDateTime timestamp;

   public Long getId() {
      return id;
   }

   public PaymentDetails setId(Long id) {
      this.id = id;
      return this;
   }

   public Person getPerson() {
      return person;
   }

   public PaymentDetails setPerson(Person person) {
      this.person = person;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public PaymentDetails setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public PaymentDetails setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
   }
}
