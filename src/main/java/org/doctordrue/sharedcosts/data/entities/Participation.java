package org.doctordrue.sharedcosts.data.entities;

import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "participation")
public class Participation implements ISharedAmount<Total> {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "name")
   private String name;

   @Column(name = "amount", nullable = false)
   private Double amount;

   @ManyToOne(optional = false)
   private Cost cost;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "person_participation")
   private Set<Person> people = new HashSet<>();

   public Long getId() {
      return id;
   }

   public Participation setId(Long id) {
      this.id = id;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public Participation setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public String getName() {
      return name;
   }

   public Participation setName(String name) {
      this.name = name;
      return this;
   }

   public Cost getCost() {
      return cost;
   }

   public Participation setCost(Cost cost) {
      this.cost = cost;
      return this;
   }

   @Override
   public Currency getCurrency() {
      return this.getCost().getCurrency();
   }

   @Override
   public Set<Person> getPeople() {
      return people;
   }

   @Override
   public List<Total> getFlatAmounts() {
      double singleAmount = this.amount / this.people.size();
      return this.getPeople().stream().map(p -> new Total()
                      .setCurrency(this.getCurrency())
                      .setAmount(singleAmount)
                      .setPerson(p))
              .collect(Collectors.toList());
   }

   public Participation setPeople(Set<Person> people) {
      this.people = people;
      return this;
   }

   public Participation addPerson(Person person) {
      this.people.add(person);
      return this;
   }

   public Set<Person> getPotentialParticipants() {
      return this.getCost().getGroup().getParticipants().stream()
              .filter(p -> !this.getPeople().contains(p))
              .collect(Collectors.toSet());
   }

   public String toTelegramString() {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("%s = %.2f %s: ", this.getName(), this.getAmount(), this.getCurrency()));
      if (this.getPeople().isEmpty()) {
         sb.append(" - не распределено!");
      } else {
         sb.append(this.getPeople()
                 .stream()
                 .map(Person::toTelegramString)
                 .collect(Collectors.joining(", ")));
      }
      return sb.toString();
   }
}
