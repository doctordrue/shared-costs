package org.doctordrue.sharedcosts.exceptions.cost;

import org.doctordrue.sharedcosts.exceptions.IErrorMessage;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public enum CostError implements IErrorMessage {
   NOT_FOUND_BY_ID("COST-001", "Cost not found for id=${id}"),
   NOT_FOUND_BY_NAME(null, "Чек с именем '{name} не найден'"),
   ALREADY_EXIST(null, "Чек с именем ${name} уже есть в системе. Используйте другое название");

   private final String code;
   private final String template;

   CostError(String code, String template) {
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
