package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.repositories.GroupRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.group.GroupNotFoundException;
import org.doctordrue.sharedcosts.exceptions.group.ParticipantBusyInCostsException;
import org.doctordrue.sharedcosts.exceptions.people.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class GroupService {
   @Autowired
   private GroupRepository groupRepository;
   @Autowired
   private CostService costService;
   @Autowired
   private PersonService personService;

   @PostFilter("hasRole('ADMIN') or filterObject.isParticipated(principal.username)")
   public List<Group> findAll() {
      return this.groupRepository.findAll();
   }

   @PostAuthorize("hasRole('ADMIN') or returnObject.isParticipated(principal.username)")
   public Group findById(Long id) {
      return this.groupRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   @PostFilter("hasRole('ADMIN') or filterObject.isParticipated(principal.username)")
   public List<Group> findByName(String name) {
      Group probe = new Group().setName(name);
      return this.groupRepository.findAll(Example.of(probe));
   }

   public Group create(Group group) {
      return this.groupRepository.save(group);
   }

   @Transactional
   public Group update(Long id, Group group) {
      assumeExists(id);
      Group persistedGroup = this.groupRepository.getById(id);

      group.setId(id);
      if (group.getParticipants().isEmpty()) {
         group.setParticipants(persistedGroup.getParticipants());
      }
      return this.groupRepository.save(group);
   }

   @Transactional
   public Group addParticipant(Long id, String username) {
      Group persistedGroup = this.findById(id);
      Person person = this.personService.findByUsername(username);
      if (person == null) {
         throw new PersonNotFoundException(username);
      }
      persistedGroup.getParticipants().add(person);
      return this.groupRepository.save(persistedGroup);
   }

   public Group deleteParticipant(Long id, String username) {
      Group persistedGroup = this.findById(id);
      Person persistedPerson = this.personService.findByUsername(username);
      verifyParticipateRemoval(persistedGroup, persistedPerson);
      persistedGroup.getParticipants().removeIf(persistedPerson::equals);
      return this.groupRepository.save(persistedGroup);
   }

   private void verifyParticipateRemoval(Group group, Person participant) {
      List<Cost> costs = group.getCosts().stream()
              .filter(c -> c.getPayments().stream().anyMatch(p -> Objects.equals(p.getPerson().getUsername(), participant.getUsername())) ||
                      c.getParticipations().stream().anyMatch(p -> Objects.equals(p.getPerson().getUsername(), participant.getUsername())))
              .collect(Collectors.toList());
      if (!costs.isEmpty()) {
         throw new ParticipantBusyInCostsException(group.getId(), participant, costs);
      }
   }

   public List<Person> findPeopleToParticipateIn(Group group) {
      return this.personService.findAll()
              .stream()
              .filter(p -> group.getParticipants()
                      .stream()
                      .noneMatch(e -> e.equals(p)))
              .collect(Collectors.toList());
   }

   public void delete(Long id) {
      assumeExists(id);
      this.groupRepository.deleteById(id);
   }

   @Transactional
   public void deleteRecursively(Long id) {
      assumeExists(id);
      List<Long> costIds = this.costService.findAllByGroupId(id).stream().map(Cost::getId).collect(Collectors.toList());
      this.costService.deleteAllRecursively(costIds);
      this.groupRepository.deleteById(id);
   }

   private void assumeExists(Long id) {
      if (!this.groupRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new GroupNotFoundException(id);
   }
}
