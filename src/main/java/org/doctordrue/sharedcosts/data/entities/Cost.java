package org.doctordrue.sharedcosts.data.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "costs")
public class Cost {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "total", nullable = false)
   private Double total;

   @Column(name = "datetime", nullable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime datetime;

   @ManyToOne(optional = false, fetch = FetchType.LAZY)
   private Group group;

   @ManyToOne(optional = false, fetch = FetchType.LAZY)
   private Currency currency;

   @OneToMany(mappedBy = "cost")
   @OrderBy("id")
   private List<Payment> payments;

   @OneToMany(mappedBy = "cost")
   @OrderBy("id")
   private List<Participation> participations;

   public Long getId() {
      return id;
   }

   public Cost setId(Long id) {
      this.id = id;
      return this;
   }

   public String getName() {
      return name;
   }

   public Cost setName(String name) {
      this.name = name;
      return this;
   }

   public Group getGroup() {
      return group;
   }

   public Cost setGroup(Group group) {
      this.group = group;
      return this;
   }

   public Currency getCurrency() {
      return currency;
   }

   public Cost setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public Double getTotal() {
      return total;
   }

   public Cost setTotal(Double total) {
      this.total = total;
      return this;
   }

   public LocalDateTime getDatetime() {
      return datetime;
   }

   public Cost setDatetime(LocalDateTime datetime) {
      this.datetime = datetime;
      return this;
   }

   public List<Payment> getPayments() {
      return payments;
   }

   public Cost setPayments(List<Payment> payments) {
      this.payments = payments;
      return this;
   }

   public List<Participation> getParticipations() {
      return participations;
   }

   public Cost setParticipations(List<Participation> participations) {
      this.participations = participations;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Cost cost = (Cost) o;

      return getId().equals(cost.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Cost.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("name='" + name + "'")
              .add("datetime=" + datetime)
              .toString();
   }
}
