package org.doctordrue.sharedcosts.data.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.doctordrue.sharedcosts.utils.PasswordGeneratorUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.telegram.telegrambots.meta.api.objects.User;

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

   @Column(name = "username", unique = true, nullable = false)
   private String username;

   @Column(name = "telegram_id", unique = true)
   private Long telegramId;

   @Column(name = "password")
   private String password;

   @Column(name = "first_name")
   private String firstName;

   @Column(name = "last_name")
   private String lastName;

   @Column(name = "phone_number")
   private String phoneNumber;

   @Column(name = "enabled", nullable = false)
   private Boolean enabled = true;

   @Column(name = "locked", nullable = false)
   private Boolean locked = false;

   @Column(name = "role", nullable = false)
   @Enumerated(EnumType.STRING)
   private RoleType role = RoleType.ANONYMOUS;

   @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER)
   private Set<Group> groups = new java.util.LinkedHashSet<>();

   public Set<Group> getGroups() {
      return groups;
   }

   public void setGroups(Set<Group> groups) {
      this.groups = groups;
   }

   public Long getId() {
      return id;
   }

   public Long getTelegramId() {
      return telegramId;
   }

   public Person setTelegramId(Long telegramId) {
      this.telegramId = telegramId;
      return this;
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

   public Person setUsername(String username) {
      this.username = username;
      return this;
   }

   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return this.username;
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

   public String getFullName() {
      StringBuilder builder = new StringBuilder();
      if (StringUtils.isNotBlank(this.firstName)) {
         builder.append(firstName);
      }
      if (StringUtils.isNotBlank(this.lastName)) {
         if (builder.length() > 0) {
            builder.append(" ");
         }
         builder.append(this.lastName);
      }
      if (builder.length() == 0) {
         builder.append(this.username);
      }
      return builder.toString();
   }

   public String getShortFullName() {
      StringBuilder builder = new StringBuilder();
      if (StringUtils.isNotBlank(this.firstName)) {
         builder.append(firstName);
      }
      if (StringUtils.isNotBlank(this.lastName)) {
         if (builder.length() > 0) {
            builder.append(" ");
         }
         builder.append(this.lastName.substring(0, 1).toUpperCase()).append(".");
      }
      if (builder.length() == 0) {
         builder.append(this.username);
      }
      return builder.toString();
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return this.locked == null || !this.locked;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled == null || this.enabled;
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return Collections.singletonList(role);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!(o instanceof Person))
         return false;

      Person person = (Person) o;

      if (!getId().equals(person.getId()))
         return false;
      return getUsername().equals(person.getUsername());
   }

   public boolean hasTelegramId() {
      return this.telegramId != null;
   }

   public static Person fromTelegram(User user) {
      String identifier = StringUtils.isNotBlank(user.getUserName()) ? user.getUserName() : user.getId().toString();
      String tempPassword = PasswordGeneratorUtil.generate();
      return new Person().setRole(RoleType.USER)
              .setUsername(identifier)
              .setFirstName(user.getFirstName())
              .setLastName(user.getLastName())
              .setTelegramId(user.getId())
              .setPassword(tempPassword);
   }

   @Override
   public int hashCode() {
      int result = getId().hashCode();
      result = 31 * result + getUsername().hashCode();
      return result;
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("email='" + username + "'")
              .add("firstName='" + firstName + "'")
              .add("lastName='" + lastName + "'")
              .add("phoneNumber='" + phoneNumber + "'")
              .add("email='" + username + "'")
              .add("isEnabled=" + enabled)
              .add("isLocked=" + locked)
              .toString();
   }
}
