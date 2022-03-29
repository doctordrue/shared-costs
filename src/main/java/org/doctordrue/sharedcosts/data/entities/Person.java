package org.doctordrue.sharedcosts.data.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "persons")
public class Person implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name="email", unique = true, nullable = false)
   private String email;

   @Column(name = "password")
   private String password;

   @Column(name = "first_name")
   private String firstName;

   @Column(name = "last_name")
   private String lastName;

   @Column(name = "phone_number")
   private String phoneNumber;

   @Column(name="enabled", nullable = false)
   private Boolean enabled = true;

   @Column(name="locked", nullable = false)
   private Boolean locked = true;

   @Column(name = "role", nullable = false)
   @Enumerated(EnumType.STRING)
   private RoleType role = RoleType.ANONYMOUS;

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

   public String getEmail() {
      return email;
   }

   public Person setEmail(String email) {
      this.email = email;
      return this;
   }

   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return getEmail();
   }

   public Person setPassword(String password) {
      this.password = password;
      return this;
   }

   public Boolean getEnabled() {
      return enabled;
   }

   public Person setEnabled(Boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public Boolean getLocked() {
      return locked;
   }

   public Person setLocked(Boolean locked) {
      this.locked = locked;
      return this;
   }

   public RoleType getRole() {
      return role;
   }

   public Person setRole(RoleType role) {
      this.role = role;
      return this;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return this.locked == null ? true : this.locked;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled == null ? true : this.enabled;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Collections.singletonList(role);
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("email='" + email + "'")
              .add("firstName='" + firstName + "'")
              .add("lastName='" + lastName + "'")
              .add("phoneNumber='" + phoneNumber + "'")
              .add("email='" + email + "'")
              .add("isEnabled=" + enabled)
              .add("isLocked=" + locked)
              .toString();
   }
}
