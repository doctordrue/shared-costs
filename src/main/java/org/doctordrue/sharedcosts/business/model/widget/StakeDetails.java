package org.doctordrue.sharedcosts.business.model.widget;

import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class StakeDetails {

   private Long id;
   private Person person;
   private Double amount;
   private Long costId;

   public Long getId() {
      return id;
   }

   public StakeDetails setId(Long id) {
      this.id = id;
      return this;
   }

   public Person getPerson() {
      return person;
   }

   public StakeDetails setPerson(Person person) {
      this.person = person;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public StakeDetails setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Long getCostId() {
      return costId;
   }

   public StakeDetails setCostId(Long costId) {
      this.costId = costId;
      return this;
   }
}
