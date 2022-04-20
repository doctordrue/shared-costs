package org.doctordrue.sharedcosts.exceptions.group;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 4/20/2022
 **/
public class ParticipantBusyInCostsException extends BaseGroupServiceException {

   private final Long groupId;

   public ParticipantBusyInCostsException(Long groupId, Person person, List<Cost> costs) {
      super(GroupError.PERSON_PARTICIPATED_IN_COSTS);
      this.setParameter("fullname", person.getFullName());
      this.setParameter("costs", costs.stream().map(Cost::getName).collect(Collectors.joining(", ")));
      this.groupId = groupId;
   }

   public Long getGroupId() {
      return groupId;
   }
}
