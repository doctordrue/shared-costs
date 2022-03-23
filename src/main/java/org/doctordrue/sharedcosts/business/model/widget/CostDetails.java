package org.doctordrue.sharedcosts.business.model.widget;

import java.time.LocalDateTime;
import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Payment;
import org.doctordrue.sharedcosts.data.entities.Stake;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
public class CostDetails {

   private Long id;
   private String name;
   private Double amount;
   private Currency currency;
   private LocalDateTime timestamp;
   private List<StakeDetails> stakes;
   private List<PaymentDetails> payments;

   public Long getId() {
      return id;
   }

   public CostDetails setId(Long id) {
      this.id = id;
      return this;
   }

   public List<StakeDetails> getStakes() {
      return stakes;
   }

   public CostDetails setStakes(List<StakeDetails> stakes) {
      this.stakes = stakes;
      return this;
   }

   public List<PaymentDetails> getPayments() {
      return payments;
   }

   public CostDetails setPayments(List<PaymentDetails> payments) {
      this.payments = payments;
      return this;
   }

   public String getName() {
      return name;
   }

   public CostDetails setName(String name) {
      this.name = name;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public CostDetails setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public Currency getCurrency() {
      return currency;
   }

   public CostDetails setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public CostDetails setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
   }
}
