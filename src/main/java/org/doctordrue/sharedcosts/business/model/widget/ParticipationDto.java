package org.doctordrue.sharedcosts.business.model.widget;

import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class ParticipationDto {

   private Long id;
   private Person person;
   private Double amount;
   private String name;

   public Long getId() {
      return id;
   }

   public ParticipationDto setId(Long id) {
      this.id = id;
      return this;
   }

   public Person getPerson() {
      return person;
   }

   public ParticipationDto setPerson(Person person) {
      this.person = person;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public ParticipationDto setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public String getName() {
      return name;
   }

   public ParticipationDto setName(String name) {
      this.name = name;
      return this;
   }

   public static ParticipationDto from(Participation participation, Person person) {
      return new ParticipationDto()
              .setId(participation.getId())
              .setName(participation.getName())
              .setAmount(participation.getAmount())
              .setPerson(person);
   }
}
