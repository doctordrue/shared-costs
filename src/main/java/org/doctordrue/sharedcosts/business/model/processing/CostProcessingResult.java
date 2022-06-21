package org.doctordrue.sharedcosts.business.model.processing;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Payment;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
public class CostProcessingResult {

   private Cost cost;
   private List<Payment> payments;
   private List<Participation> participations;

   public Cost getCost() {
      return cost;
   }

   public CostProcessingResult setCost(Cost cost) {
      this.cost = cost;
      return this;
   }

   public List<Payment> getPayments() {
      return payments;
   }

   public CostProcessingResult setPayments(List<Payment> payments) {
      this.payments = payments;
      return this;
   }

   public List<Participation> getParticipations() {
      return participations;
   }

   public CostProcessingResult setParticipations(List<Participation> participations) {
      this.participations = participations;
      return this;
   }
}
