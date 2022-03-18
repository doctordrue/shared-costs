package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Stake;
import org.doctordrue.sharedcosts.data.repositories.StakeRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
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

   public Stake findById(Long id) {
      return this.stakeRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public Stake create(Stake stake) {
      return this.stakeRepository.save(stake);
   }

   public List<Stake> create(List<Stake> stakes) {
      return this.stakeRepository.saveAll(stakes);
   }

   public Stake update(Long id, Stake stake) {
      assumeExists(id);
      stake.setId(id);
      return this.stakeRepository.save(stake);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.stakeRepository.deleteById(id);
   }

   private void assumeExists(Long id) {
      if (!this.stakeRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new BaseException("SC200", "Stake not found for id = " + id);
   }
}
