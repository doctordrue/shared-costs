package org.doctordrue.sharedcosts.exceptions.group;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class BaseGroupServiceException extends BaseException {

   public BaseGroupServiceException(GroupError error) {
      super(error);
   }

   public BaseGroupServiceException(GroupError error, Throwable cause) {
      super(error, cause);
   }
}
