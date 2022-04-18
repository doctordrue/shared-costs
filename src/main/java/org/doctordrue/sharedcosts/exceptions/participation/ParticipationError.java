package org.doctordrue.sharedcosts.exceptions.participation;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum ParticipationError implements IErrorMessage {
   NOT_FOUND_BY_ID("PART-0001", "Participation not found for id=${id}");

   private final String code;
   private final String template;

   ParticipationError(String code, String template) {
      this.code = code;
      this.template = template;
   }

   @Override
   public String getCode() {
      return code;
   }

   @Override
   public String getTemplate() {
      return template;
   }
}
