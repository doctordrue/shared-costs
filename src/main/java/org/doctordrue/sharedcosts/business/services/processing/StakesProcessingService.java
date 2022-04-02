package org.doctordrue.sharedcosts.business.services.processing;

import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Stake;
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
   private StakeService stakeService;
   @Autowired
   private CostProcessingService costProcessingService;

   @Transactional
   public Stake processNewStake(Stake stake, boolean isUpdateCost) {
      Stake persistedStake = this.stakeService.create(stake);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedStake.getCostId());
      }
      return persistedStake;
   }

   @Transactional
   public Stake processEditStake(Stake stake, boolean isUpdateCost) {
      Stake persistedStake = this.stakeService.update(stake.getId(), stake);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedStake.getCostId());
      }
      return persistedStake;
   }

   @Transactional
   public void processDelete(Long id, boolean isUpdateCost) {
      Stake persistedStake = this.stakeService.findById(id);
      this.stakeService.delete(id);
      if (isUpdateCost) {
         this.costProcessingService.updateCostTotalFromStakes(persistedStake.getCostId());
      }
   }
}
