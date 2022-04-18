package org.doctordrue.sharedcosts.business.model.webform.people;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class PasswordUpdateInputData {

   private String old;
   private String updated;
   private String confirm;

   public String getOld() {
      return old;
   }

   public PasswordUpdateInputData setOld(String old) {
      this.old = old;
      return this;
   }

   public String getUpdated() {
      return updated;
   }

   public PasswordUpdateInputData setUpdated(String updated) {
      this.updated = updated;
      return this;
   }

   public String getConfirm() {
      return confirm;
   }

   public PasswordUpdateInputData setConfirm(String confirm) {
      this.confirm = confirm;
      return this;
   }
}
