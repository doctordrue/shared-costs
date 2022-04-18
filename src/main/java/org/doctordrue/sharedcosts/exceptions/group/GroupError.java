package org.doctordrue.sharedcosts.exceptions.group;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum GroupError implements IErrorMessage {
   NOT_FOUND_BY_ID("GRPS-001", "Costs group is not found for id=${id}");

   private final String code;
   private final String template;

   GroupError(String code, String template) {
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
