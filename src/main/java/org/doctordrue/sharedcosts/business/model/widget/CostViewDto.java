package org.doctordrue.sharedcosts.business.model.widget;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 4/1/2022
 **/
public class CostViewDto implements Serializable {

   private Long id;
   private String name;
   private Double amount;
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
   private LocalDateTime timestamp;
   private Currency currency;
   private List<PaymentDto> payments;
   private List<StakeDto> stakes;
   private List<Person> availableForPaymentPeople;
   private List<Person> availableForStakePeople;
   private CostGroup costGroup;

   public static class Builder {

      private final CostViewDto dto;

      public Builder(Cost cost) {
         this.dto = new CostViewDto();
         this.dto.id = cost.getId();
         this.dto.name = cost.getName();
         this.dto.amount = cost.getCostTotal();
         this.dto.timestamp = cost.getCostDateTime();
      }

      public Builder setCurrency(Currency currency) {
         this.dto.currency = currency;
         return this;
      }

      public Builder setPayments(List<PaymentDto> payments) {
         this.dto.payments = payments;
         return this;
      }

      public Builder setStakes(List<StakeDto> stakes) {
         this.dto.stakes = stakes;
         return this;
      }

      public Builder setAvailableForPaymentPeople(List<Person> people) {
         this.dto.availableForPaymentPeople = people;
         return this;
      }

      public Builder setAvailableForStakePeople(List<Person> people) {
         this.dto.availableForStakePeople = people;
         return this;
      }

      public Builder setCostGroup(CostGroup group) {
         this.dto.costGroup = group;
         return this;
      }

      public CostViewDto build() {
         return dto;
      }
   }

   private CostViewDto() {
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public Double getAmount() {
      return amount;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public Currency getCurrency() {
      return currency;
   }

   public List<PaymentDto> getPayments() {
      return payments;
   }

   public List<StakeDto> getStakes() {
      return stakes;
   }

   public List<Person> getAvailableForPaymentPeople() {
      return availableForPaymentPeople;
   }

   public List<Person> getAvailableForStakePeople() {
      return availableForStakePeople;
   }

   public CostGroup getCostGroup() {
      return costGroup;
   }
}