package org.doctordrue.sharedcosts.exceptions.participation;

import org.doctordrue.sharedcosts.exceptions.BaseException;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class BaseParticipationServiceException extends BaseException {

   public BaseParticipationServiceException(ParticipationError error) {
      super(error);
   }

   public BaseParticipationServiceException(ParticipationError error, Throwable cause) {
      super(error, cause);
   }
}
