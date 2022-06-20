package org.doctordrue.sharedcosts.exceptions.group;

import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 6/17/2022
 **/
public class ParticipantNotFoundException extends BaseException {

   public ParticipantNotFoundException(String userName, Group group) {
      super(GroupError.PERSON_NOT_FOUND_IN_GROUP_PARTICIPANTS);
      setParameter("userName", userName);
      setParameter("groupName", group.getName());
   }
}
