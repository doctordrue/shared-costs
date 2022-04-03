package org.doctordrue.sharedcosts.data.entities;

import javax.persistence.*;

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

   @Column(name = "name")
   private String name;

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

   public String getName() {
      return name;
   }

   public Payment setName(String name) {
      this.name = name;
      return this;
   }
}
