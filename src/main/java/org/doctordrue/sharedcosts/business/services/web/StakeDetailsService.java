package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Person;
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
   private StakeService stakeService;
   @Autowired
   private PersonService personService;
   @Autowired
   private CostService costService;
   @Autowired
   private CurrencyService currencyService;

   public List<StakeDto> findAllDetails(Cost cost) {
      return this.stakeService.findAllByCostId(cost.getId()).stream()
              .map(this::convertFrom)
              .collect(Collectors.toList());
   }

   public StakeDto findDetails(Long stakeId) {
      Stake stake = this.stakeService.findById(stakeId);
      return this.convertFrom(stake);
   }

   private StakeDto convertFrom(Stake stake) {
      //TODO: refactor to reduce calls to DB
      Person person = this.personService.findById(stake.getPersonId());
      return new StakeDto()
              .setId(stake.getId())
              .setAmount(stake.getStakeTotal())
              .setPerson(person);
   }
}
