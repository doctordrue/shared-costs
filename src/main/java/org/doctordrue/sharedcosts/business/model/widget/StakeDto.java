package org.doctordrue.sharedcosts.business.model.widget;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Stake;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class StakeDto {

   private Long id;
   private Person person;
   private Double amount;
   private String name;

   public Long getId() {
      return id;
   }

   public StakeDto setId(Long id) {
      this.id = id;
      return this;
   }

   public Person getPerson() {
      return person;
   }

   public StakeDto setPerson(Person person) {
      this.person = person;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public StakeDto setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public String getName() {
      return name;
   }

   public StakeDto setName(String name) {
      this.name = name;
      return this;
   }

   public static StakeDto from(Stake stake, Person person) {
      return new StakeDto()
              .setId(stake.getId())
              .setName(stake.getName())
              .setAmount(stake.getStakeTotal())
              .setPerson(person);
   }
}
