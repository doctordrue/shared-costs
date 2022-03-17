package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.repositories.CostRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class CostService {

   private final CostRepository costRepository;

   public CostService(CostRepository costRepository) {
      this.costRepository = costRepository;
   }

   public List<Cost> findAll() {
      return this.costRepository.findAll(Sort.by("costDateTime"));
   }

   public Cost findById(Long id) {
      return this.costRepository.findById(id).orElseThrow(() -> new BaseException("CS002", "Cost not found for id = " + id));
   }

   public List<Cost> findAllByGroupId(Long groupId) {
      Cost probe = new Cost().setGroupId(groupId);
      return this.costRepository.findAll(Example.of(probe));
   }

   public Cost create(Cost cost) {
      return this.costRepository.save(cost);
   }

   public Cost update(Long id, Cost newCost) {
      if (!this.costRepository.existsById(id)) {
         throw new BaseException("CS002", "Cost not found for id = " + id);
      }
      newCost.setId(id);
      return this.costRepository.save(newCost);
   }

   public void delete(Long id) {
      this.costRepository.deleteById(id);
   }
}
