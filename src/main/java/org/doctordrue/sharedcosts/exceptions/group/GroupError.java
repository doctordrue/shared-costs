package org.doctordrue.sharedcosts.exceptions.group;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum GroupError implements IErrorMessage {
   NOT_FOUND_BY_ID("GRPS-001", "Group is not found for id=${id}"),
   NOT_FOUND_BY_NAME("GRPS-002", "Группа с именем ${name} не найдена."),
   PERSON_PARTICIPATED_IN_COSTS("GRPS-002", "Unable to remove ${fullname} from group participants list. Remove from following costs first: ${costs}"),
   PERSON_NOT_FOUND_IN_GROUP_PARTICIPANTS(null, "Пользователь ${userName} не найден среди участников группы ${groupName}");

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
