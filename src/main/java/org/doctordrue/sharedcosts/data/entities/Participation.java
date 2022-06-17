package org.doctordrue.sharedcosts.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "participation")
public class Participation implements IOwnedAmount {

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

   @ManyToOne(optional = false)
   private Person person;

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

   public Person getPerson() {
      return person;
   }

   @Override
   public Currency getCurrency() {
      return this.getCost().getCurrency();
   }

   public Participation setPerson(Person person) {
      this.person = person;
      return this;
   }
}
