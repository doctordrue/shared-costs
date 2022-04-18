package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.repositories.GroupRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.group.GroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

   public List<Group> findAll() {
      return this.groupRepository.findAll();
   }

   public Group findById(Long id) {
      return this.groupRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public List<Group> findByName(String name) {
      Group probe = new Group().setName(name);
      return this.groupRepository.findAll(Example.of(probe));
   }

   public Group create(Group group) {
      return this.groupRepository.save(group);
   }

   public Group update(Long id, Group group) {
      assumeExists(id);
      Group persistedGroup = this.groupRepository.getById(id);

      group.setId(id);
      if (group.getParticipants().isEmpty()) {
         group.setParticipants(persistedGroup.getParticipants());
      }
//      if (group.getCosts().isEmpty()) {
//         group.setCosts(persistedGroup.getCosts());
//      }
      return this.groupRepository.save(group);
   }

   public Group addParticipant(Long id, Long personId) {
      Group persistedGroup = this.findById(id);
      Person person = this.personService.findById(personId);
      persistedGroup.getParticipants().add(person);
      return this.groupRepository.save(persistedGroup);
   }

   public Group deleteParticipant(Long id, Long personId) {
      Group persistedGroup = this.findById(id);
      persistedGroup.getParticipants().removeIf(p -> p.getId().equals(personId));
      return this.groupRepository.save(persistedGroup);
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
