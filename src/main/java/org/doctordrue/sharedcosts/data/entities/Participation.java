package org.doctordrue.sharedcosts.data.entities;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;

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

   @ManyToMany
   @JoinTable(name = "person_participation")
   private Set<Person> people;

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
}
