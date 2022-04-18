package org.doctordrue.sharedcosts.exceptions.people;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum PeopleError implements IErrorMessage {
   NOT_FOUND_BY_ID("PRSN-001", "Person not found for id=${id}"),
   ALREADY_EXISTS("PRSN-002", "Person already exists: ${username}"),
   PASSWORD_NOT_ACCEPTED("PRSN-003", "Current password is not accepted"),
   PASSWORD_NOT_CONFIRMED("PRSN-004", "New password and confirmation is not equal");

   private final String code;
   private final String template;

   PeopleError(String code, String template) {
      this.code = code;
      this.template = template;
   }

   @Override
   public String getCode() {
      return this.code;
   }

   @Override
   public String getTemplate() {
      return this.template;
   }

}
