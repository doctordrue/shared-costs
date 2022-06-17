package org.doctordrue.sharedcosts.exceptions.group;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class GroupNotFoundException extends BaseGroupServiceException {

   public GroupNotFoundException(Long id) {
      super(GroupError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   public GroupNotFoundException(Long id, Throwable cause) {
      super(GroupError.NOT_FOUND_BY_ID, cause);
      setParameter("id", id);
   }

   public GroupNotFoundException(String name) {
      super(GroupError.NOT_FOUND_BY_NAME);
      setParameter("name", name);
   }
}
