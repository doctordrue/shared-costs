package org.doctordrue.sharedcosts.exceptions.participation;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class ParticipationNotFoundException extends BaseParticipationServiceException {

   public ParticipationNotFoundException(Long id) {
      super(ParticipationError.NOT_FOUND_BY_ID);
      setParameter("id", id);
   }

   public ParticipationNotFoundException(Long id, Throwable cause) {
      super(ParticipationError.NOT_FOUND_BY_ID, cause);
      setParameter("id", id);
   }
}
