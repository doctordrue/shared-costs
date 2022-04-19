package org.doctordrue.sharedcosts.exceptions.group;

import org.doctordrue.sharedcosts.data.entities.Person;

/**
 * @author Andrey_Barantsev
 * 4/19/2022
 **/
public class UnableToDeleteParticipantException extends BaseGroupServiceException {

   private final Long groupId;
   private final Person person;

   public UnableToDeleteParticipantException(Long groupId, Person person) {
      super(GroupError.CANNOT_DELETE_PARTICIPANT);
      this.groupId = groupId;
      this.person = person;
      setParameter("fullname", person.getFullName());
   }

   public Long getGroupId() {
      return groupId;
   }

   public Person getPerson() {
      return person;
   }
}
