package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.repositories.CostRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.cost.CostNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class CostService {

   private final CostRepository costRepository;
   private final ParticipationService participationService;
   private final PaymentService paymentService;

   public CostService(CostRepository costRepository, ParticipationService participationService, PaymentService paymentService) {
      this.costRepository = costRepository;
      this.participationService = participationService;
      this.paymentService = paymentService;
   }

   @PostFilter("hasRole('ADMIN') or filterObject.group.isParticipated(principal.username)")
   public List<Cost> findAll() {
      return this.costRepository.findAll(Sort.by("datetime"));
   }

   @PostAuthorize("hasRole('ADMIN') or returnObject.group.isParticipated(principal.username)")
   public Cost findById(Long id) {
      return this.costRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   @PostFilter("hasRole('ADMIN') or filterObject.group.isParticipated(principal.username)")
   public List<Cost> findAllByGroupId(Long groupId) {
      return this.costRepository.findByGroupId(groupId);
   }

   public Optional<Cost> findByName(String name) {
      return this.costRepository.findByName(name);
   }

   public boolean existsByName(String name) {
      return this.costRepository.existsByName(name);
   }

   public Cost create(Cost cost) {
      setDateTimeIfNull(cost);
      setTotalIfNull(cost);
      return this.costRepository.save(cost);
   }

   public Cost update(Long id, Cost newCost) {
      assumeExists(id);
      newCost.setId(id);
      return this.costRepository.save(newCost);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.costRepository.deleteById(id);
   }

   public void deleteAll(List<Long> ids) {
      this.costRepository.deleteAllByIdInBatch(ids);
   }

   @Transactional
   public void deleteRecursively(Long id) {
      assumeExists(id);
      this.participationService.deleteAllForCost(id);
      this.paymentService.deleteAllForCost(id);
      this.delete(id);
   }

   @Transactional
   public void deleteAllRecursively(List<Long> ids) {
      this.costRepository.findAllById(ids)
              .forEach(cost -> {
                 this.participationService.deleteAllForCost(cost.getId());
                 this.paymentService.deleteAllForCost(cost.getId());});
      this.deleteAll(ids);
   }

   private void assumeExists(Long id) {
      if (!this.costRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new CostNotFoundException(id);
   }

   private void setDateTimeIfNull(Cost cost) {
      if (cost.getDatetime() == null) {
         cost.setDatetime(LocalDateTime.now());
      }
   }

   private void setTotalIfNull(Cost cost) {
      if (cost.getTotal() == null) {
         cost.setTotal(0d);
      }
   }
}
