package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.doctordrue.sharedcosts.business.model.widget.CostGroupDetails;
import org.doctordrue.sharedcosts.business.model.widget.PaymentDto;
import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/23/2022
 **/
@Service
public class PersonWebService {
   @Autowired
   private PersonService personService;
   @Autowired
   private CostGroupDetailsService costGroupDetailsService;
   @Autowired
   private CostGroupService costGroupService;

   public List<Person> findParticipants(Long groupId) {
      CostGroup group = this.costGroupService.findById(groupId);
      CostGroupDetails details = this.costGroupDetailsService.getDetails(group);
      List<Long> participantsIds = details.getCosts().stream()
              .flatMap(costDetails -> Stream.concat(
                      costDetails.getPayments().stream().map(PaymentDto::getPerson),
                      costDetails.getStakes().stream().map(StakeDto::getPerson)))
              .map(Person::getId)
              .collect(Collectors.toList());
      return this.personService.findAll().stream()
              .filter(person -> participantsIds.contains(person.getId()))
              .collect(Collectors.toList());
   }

   public List<CostGroup> findParticipatedGroups(Long personId) {
      return this.costGroupDetailsService.getDetails().stream()
              .filter(group -> group.getCosts().stream()
                      .anyMatch(cost ->
                              cost.getPayments().stream().anyMatch(payment -> payment.getPerson().getId().equals(personId)) ||
                              cost.getStakes().stream().anyMatch(stake -> stake.getPerson().getId().equals(personId))))
              .map(CostGroupDetails::getGroup)
              .collect(Collectors.toList());
   }
}
