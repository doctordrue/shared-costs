package org.doctordrue.sharedcosts.data.entities;

import java.time.LocalDate;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "costs_groups")
public class CostGroup {

   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "description")
   private String description;

   @Column(name = "start_date", nullable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate startDate;

   @Column(name = "end_date")
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate endDate;

   public Long getId() {
      return id;
   }

   public CostGroup setId(Long id) {
      this.id = id;
      return this;
   }

   public String getName() {
      return name;
   }

   public CostGroup setName(String name) {
      this.name = name;
      return this;
   }

   public String getDescription() {
      return description;
   }

   public CostGroup setDescription(String description) {
      this.description = description;
      return this;
   }

   public LocalDate getStartDate() {
      return startDate;
   }

   public CostGroup setStartDate(LocalDate startDate) {
      this.startDate = startDate;
      return this;
   }

   public LocalDate getEndDate() {
      return endDate;
   }

   public CostGroup setEndDate(LocalDate endDate) {
      this.endDate = endDate;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      CostGroup costGroup = (CostGroup) o;

      return getId().equals(costGroup.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", CostGroup.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("name='" + name + "'")
              .add("description='" + description + "'")
              .add("startDate=" + startDate)
              .add("endDate=" + endDate)
              .toString();
   }
}
