package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Stake;
import org.doctordrue.sharedcosts.data.repositories.StakeRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class StakeService {

   private final StakeRepository stakeRepository;

   public StakeService(StakeRepository stakeRepository) {
      this.stakeRepository = stakeRepository;
   }

   public List<Stake> findAll() {
      return this.stakeRepository.findAll();
   }

   public List<Stake> findAllByCostId(Long costId) {
      Stake probe = new Stake().setCostId(costId);
      return this.stakeRepository.findAll(Example.of(probe));
   }
}
