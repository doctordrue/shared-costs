package org.doctordrue.sharedcosts.business.model.webform.processing;

import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 4/12/2022
 **/
public class SplitInputData {

   private String name;
   private Double amount;
   private Set<Person> people;

   public String getName() {
      return name;
   }

   public SplitInputData setName(String name) {
      this.name = name;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public SplitInputData setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Set<Person> getPeople() {
      return people;
   }

   public SplitInputData setPeople(Set<Person> people) {
      this.people = people;
      return this;
   }
}
