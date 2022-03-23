package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.StakeDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Stake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Service
public class StakeDetailsService {
   @Autowired
   private PersonService personService;
   @Autowired
   private StakeService stakeService;

   public List<StakeDetails> getStakesDetails(Long costId) {
      return this.stakeService.findAllByCostId(costId).stream()
              .map(this::convertFrom)
              .collect(Collectors.toList());
   }

   public StakeDetails getStakeDetails(Long stakeId) {
      Stake stake = this.stakeService.findById(stakeId);
      return this.convertFrom(stake);
   }

   private StakeDetails convertFrom(Stake stake) {
      return new StakeDetails().setId(stake.getId())
              .setAmount(stake.getStakeTotal())
              .setPerson(this.personService.findById(stake.getPersonId()));
   }
}
