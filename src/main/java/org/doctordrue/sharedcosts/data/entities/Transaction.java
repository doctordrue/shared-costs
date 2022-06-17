package org.doctordrue.sharedcosts.data.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @ManyToOne(optional = false)
   private Person from;

   @ManyToOne(optional = false)
   private Person to;

   @Column(name = "amount", nullable = false)
   private Double amount;

   @ManyToOne(optional = false)
   private Currency currency;

   @ManyToOne(optional = false)
   private Group group;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Person getFrom() {
      return from;
   }

   public Transaction setFrom(Person from) {
      this.from = from;
      return this;
   }

   public Person getTo() {
      return to;
   }

   public Transaction setTo(Person to) {
      this.to = to;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public Transaction setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Currency getCurrency() {
      return currency;
   }

   public Transaction setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public Group getGroup() {
      return group;
   }

   public Transaction setGroup(Group group) {
      this.group = group;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Transaction that = (Transaction) o;

      return getId().equals(that.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }
}