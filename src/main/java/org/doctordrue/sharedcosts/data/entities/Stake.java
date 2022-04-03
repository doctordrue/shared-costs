package org.doctordrue.sharedcosts.data.entities;

import javax.persistence.*;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "stakes")
public class Stake {

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

   @Column(name = "stake_total", nullable = false)
   private Double stakeTotal;

   public Long getId() {
      return id;
   }

   public Stake setId(Long id) {
      this.id = id;
      return this;
   }

   public Long getCostId() {
      return costId;
   }

   public Stake setCostId(Long costId) {
      this.costId = costId;
      return this;
   }

   public Long getPersonId() {
      return personId;
   }

   public Stake setPersonId(Long personId) {
      this.personId = personId;
      return this;
   }

   public Double getStakeTotal() {
      return stakeTotal;
   }

   public Stake setStakeTotal(Double stakeTotal) {
      this.stakeTotal = stakeTotal;
      return this;
   }

   public String getName() {
      return name;
   }

   public Stake setName(String name) {
      this.name = name;
      return this;
   }
}
