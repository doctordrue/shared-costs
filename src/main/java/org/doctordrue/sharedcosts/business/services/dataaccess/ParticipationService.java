package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.repositories.ParticipationRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.participation.ParticipationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class ParticipationService {
   @Autowired
   private ParticipationRepository participationRepository;

   public List<Participation> findAll() {
      return this.participationRepository.findAll();
   }

   public List<Participation> findAllByCostId(Long costId) {
      return this.participationRepository.findByCostId(costId);
   }

   public Participation findById(Long id) {
      return this.participationRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public Participation create(Participation participation) {
      return this.participationRepository.save(participation);
   }

   public List<Participation> create(List<Participation> participations) {
      return this.participationRepository.saveAll(participations);
   }

   public Participation update(Long id, Participation participation) {
      assumeExists(id);
      participation.setId(id);
      return this.participationRepository.save(participation);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.participationRepository.deleteById(id);
   }

   public void deleteAll(List<Long> ids) {
      this.participationRepository.deleteAllByIdInBatch(ids);
   }

   public void deleteAllForCost(Long costId) {
      this.deleteAll(this.findAllByCostId(costId).stream().map(Participation::getId).collect(Collectors.toList()));
   }

   private void assumeExists(Long id) {
      if (!this.participationRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new ParticipationNotFoundException(id);
   }
}
