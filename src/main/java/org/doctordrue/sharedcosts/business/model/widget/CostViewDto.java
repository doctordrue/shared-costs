package org.doctordrue.sharedcosts.business.model.widget;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
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
   private List<ParticipationDto> participations;
   private Set<Person> participants;
   private Group group;

   public static class Builder {

      private final CostViewDto dto;

      public Builder(Cost cost) {
         this.dto = new CostViewDto();
         this.dto.id = cost.getId();
         this.dto.name = cost.getName();
         this.dto.amount = cost.getTotal();
         this.dto.timestamp = cost.getDatetime();
      }

      public Builder setCurrency(Currency currency) {
         this.dto.currency = currency;
         return this;
      }

      public Builder setPayments(List<PaymentDto> payments) {
         this.dto.payments = payments;
         return this;
      }

      public Builder setParticipations(List<ParticipationDto> stakes) {
         this.dto.participations = stakes;
         return this;
      }

      public Builder setParticipants(Set<Person> people) {
         this.dto.participants = people;
         return this;
      }

      public Builder setCostGroup(Group group) {
         this.dto.group = group;
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

   public List<ParticipationDto> getParticipations() {
      return participations;
   }

   public Set<Person> getParticipants() {
      return participants;
   }

   public Group getCostGroup() {
      return group;
   }
}