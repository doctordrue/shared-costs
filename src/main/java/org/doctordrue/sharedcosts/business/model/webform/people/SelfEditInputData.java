package org.doctordrue.sharedcosts.business.model.webform.people;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
public class SelfEditInputData {

   private String email;
   private String phoneNumber;
   private String firstName;
   private String lastName;

   public String getEmail() {
      return email;
   }

   public SelfEditInputData setEmail(String email) {
      this.email = email;
      return this;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public SelfEditInputData setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
   }

   public String getFirstName() {
      return firstName;
   }

   public SelfEditInputData setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
   }

   public String getLastName() {
      return lastName;
   }

   public SelfEditInputData setLastName(String lastName) {
      this.lastName = lastName;
      return this;
   }
}
