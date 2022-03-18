package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.repositories.CostGroupRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class CostGroupService {

   private final CostGroupRepository costGroupRepository;

   public CostGroupService(CostGroupRepository costGroupRepository) {
      this.costGroupRepository = costGroupRepository;
   }

   public List<CostGroup> findAll() {
      return this.costGroupRepository.findAll();
   }

   public CostGroup findById(Long id) {
      return this.costGroupRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public List<CostGroup> findByName(String name) {
      CostGroup probe = new CostGroup().setName(name);
      return this.costGroupRepository.findAll(Example.of(probe));
   }

   public CostGroup create(CostGroup costGroup) {
      return this.costGroupRepository.save(costGroup);
   }

   public CostGroup update(Long id, CostGroup costGroup) {
      assumeExists(id);
      costGroup.setId(id);
      return this.costGroupRepository.save(costGroup);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.costGroupRepository.deleteById(id);
   }

   private void assumeExists(Long id) {
      if (!this.costGroupRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new BaseException("CS003", "Costs group is not found for id = " + id);
   }
}
