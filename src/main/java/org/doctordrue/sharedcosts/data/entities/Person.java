package org.doctordrue.sharedcosts.data.entities;

import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "persons")
public class Person {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "first_name")
   private String firstName;

   @Column(name = "last_name")
   private String lastName;

   @Column(name = "phone_number")
   private String phoneNumber;

   public Long getId() {
      return id;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public Person setId(Long id) {
      this.id = id;
      return this;
   }

   public Person setFirstName(String firstName) {
      this.firstName = firstName;
      return this;
   }

   public Person setLastName(String lastName) {
      this.lastName = lastName;
      return this;
   }

   public Person setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("firstName='" + firstName + "'")
              .add("lastName='" + lastName + "'")
              .add("phoneNumber='" + phoneNumber + "'")
              .toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Person person = (Person) o;

      return getId().equals(person.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }
}
