package org.doctordrue.sharedcosts.data.entities;

import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "payments")
public class Payment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "cost_id", nullable = false)
   private Long costId;

   @Column(name = "person_id", nullable = false)
   private Long personId;

   @Column(name = "payment_total", nullable = false)
   private Double paymentTotal;

   public Long getId() {
      return id;
   }

   public Payment setId(Long id) {
      this.id = id;
      return this;
   }

   public Long getCostId() {
      return costId;
   }

   public Payment setCostId(Long costId) {
      this.costId = costId;
      return this;
   }

   public Long getPersonId() {
      return personId;
   }

   public Payment setPersonId(Long personId) {
      this.personId = personId;
      return this;
   }

   public Double getPaymentTotal() {
      return paymentTotal;
   }

   public Payment setPaymentTotal(Double paymentTotal) {
      this.paymentTotal = paymentTotal;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Payment payment = (Payment) o;

      return getId().equals(payment.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Payment.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("costId=" + costId)
              .add("personId=" + personId)
              .add("paymentTotal=" + paymentTotal)
              .toString();
   }
}
