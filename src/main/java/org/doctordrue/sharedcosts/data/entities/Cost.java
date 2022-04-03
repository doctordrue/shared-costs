package org.doctordrue.sharedcosts.data.entities;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.StringJoiner;

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

   @Column(name = "group_id", nullable = false)
   private Long groupId;

   @Column(name = "currency_id", nullable = false)
   private Long currencyId;

   @Column(name = "cost_total", nullable = false)
   private Double costTotal;

   @Column(name = "cost_datetime", nullable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime costDateTime;

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

   public Long getGroupId() {
      return groupId;
   }

   public Cost setGroupId(Long groupId) {
      this.groupId = groupId;
      return this;
   }

   public Long getCurrencyId() {
      return currencyId;
   }

   public Cost setCurrencyId(Long currencyId) {
      this.currencyId = currencyId;
      return this;
   }

   public Double getCostTotal() {
      return costTotal;
   }

   public Cost setCostTotal(Double costTotal) {
      this.costTotal = costTotal;
      return this;
   }

   public LocalDateTime getCostDateTime() {
      return costDateTime;
   }

   public Cost setCostDateTime(LocalDateTime costDateTime) {
      this.costDateTime = costDateTime;
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
              .add("groupId=" + groupId)
              .add("currencyId=" + currencyId)
              .add("costTotal=" + costTotal)
              .add("costDateTime=" + costDateTime)
              .toString();
   }
}
