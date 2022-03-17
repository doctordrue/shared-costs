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
@Table(name = "currency")
public class Currency {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name="short_name", nullable = false, unique = true)
   private String shortName;

   @Column(name = "full_name")
   private String fullName;

   @Column(name = "rate", nullable = false)
   private Double rate;

   public Long getId() {
      return id;
   }

   public Currency setId(Long id) {
      this.id = id;
      return this;
   }

   public String getShortName() {
      return shortName;
   }

   public Currency setShortName(String shortName) {
      this.shortName = shortName;
      return this;
   }

   public String getFullName() {
      return fullName;
   }

   public Currency setFullName(String fullName) {
      this.fullName = fullName;
      return this;
   }

   public Double getRate() {
      return rate;
   }

   public Currency setRate(Double rate) {
      this.rate = rate;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Currency currency = (Currency) o;

      return getId().equals(currency.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Currency.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("shortName=" + shortName)
              .add("fullName='" + fullName + "'")
              .add("rate=" + rate)
              .toString();
   }
}
