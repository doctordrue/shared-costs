package org.doctordrue.sharedcosts.business.services.processing;

import org.doctordrue.sharedcosts.business.services.dataaccess.ParticipationService;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrey_Barantsev
 * 4/2/2022
 **/
@Service
public class StakesProcessingService {

   @Autowired
   private ParticipationService participationService;
   @Autowired
   private CostProcessingService costProcessingService;

   @Transactional
   public Participation processNew(Participation participation, boolean isUpdateCost) {
      Participation persistedParticipation = this.participationService.create(participation);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedParticipation.getCost().getId());
      }
      return persistedParticipation;
   }

   @Transactional
   public Participation processEdit(Participation participation, boolean isUpdateCost) {
      Participation persistedParticipation = this.participationService.update(participation.getId(), participation);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedParticipation.getCost().getId());
      }
      return persistedParticipation;
   }

   @Transactional
   public void processDelete(Long id, boolean isUpdateCost) {
      Participation persistedParticipation = this.participationService.findById(id);
      this.participationService.delete(id);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedParticipation.getCost().getId());
      }
   }
}
