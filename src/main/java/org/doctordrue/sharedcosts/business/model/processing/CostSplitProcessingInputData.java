package org.doctordrue.sharedcosts.business.model.processing;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
public class CostSplitProcessingInputData {

   private String name;
   private Long costGroupId;
   private String currencyShortName;
   private Double amount;
   private LocalDateTime timestamp;
   private List<Long> stakeholdersIds;
   private List<Long> payersIds;

   public String getName() {
      return name;
   }

   public CostSplitProcessingInputData setName(String name) {
      this.name = name;
      return this;
   }

   public Long getCostGroupId() {
      return costGroupId;
   }

   public CostSplitProcessingInputData setCostGroupId(Long costGroupId) {
      this.costGroupId = costGroupId;
      return this;
   }

   public String getCurrencyShortName() {
      return currencyShortName;
   }

   public CostSplitProcessingInputData setCurrencyShortName(String currencyShortName) {
      this.currencyShortName = currencyShortName;
      return this;
   }

   public Double getAmount() {
      return amount;
   }

   public CostSplitProcessingInputData setAmount(Double amount) {
      this.amount = amount;
      return this;
   }

   public LocalDateTime getTimestamp() {
      return timestamp;
   }

   public CostSplitProcessingInputData setTimestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
   }

   public List<Long> getStakeholdersIds() {
      return stakeholdersIds;
   }

   public CostSplitProcessingInputData setStakeholdersIds(List<Long> stakeholdersIds) {
      this.stakeholdersIds = stakeholdersIds;
      return this;
   }

   public List<Long> getPayersIds() {
      return payersIds;
   }

   public CostSplitProcessingInputData setPayersIds(List<Long> payersIds) {
      this.payersIds = payersIds;
      return this;
   }
}
