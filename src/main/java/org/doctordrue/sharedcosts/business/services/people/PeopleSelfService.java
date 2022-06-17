package org.doctordrue.sharedcosts.business.services.people;

import org.doctordrue.sharedcosts.business.model.webform.people.PasswordUpdateInputData;
import org.doctordrue.sharedcosts.business.model.webform.people.SelfEditInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.doctordrue.sharedcosts.exceptions.people.PasswordNotAcceptedException;
import org.doctordrue.sharedcosts.exceptions.people.PasswordNotConfirmedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 4/15/2022
 **/
@Service
public class PeopleSelfService {

   @Autowired
   private PersonService personService;
   @Autowired
   private PasswordEncoder encoder;

   @PreAuthorize("authentication.principal.username.equals(#username)")
   public Person update(String username, SelfEditInputData data) {
      Person currentPerson = this.personService.loadUserByUsername(username);
      Person updatedPerson = new Person()
              .setEnabled(currentPerson.isEnabled())
              .setLocked(!currentPerson.isAccountNonLocked())
              .setRole(currentPerson.getRole())
              .setUsername(currentPerson.getUsername());
      Long id = currentPerson.getId();
      if (data.getEmail() != null) {
         updatedPerson.setUsername(data.getEmail());
      }
      updatedPerson.setPhoneNumber(data.getPhoneNumber())
              .setFirstName(data.getFirstName())
              .setLastName(data.getLastName());
      return this.personService.update(id, updatedPerson);
   }

   @PreAuthorize("authentication.principal.username.equals(#username)")
   public Person load(String username) {
      return this.personService.loadUserByUsername(username);
   }

   @PreAuthorize("authentication.principal.username.equals(#username)")
   public Person passwordUpdate(String username, PasswordUpdateInputData password) {
      if (!password.getUpdated().equals(password.getConfirm())) {
         throw new PasswordNotConfirmedException();
      }
      Person persistedPerson = this.personService.loadUserByUsername(username);
      if (!this.encoder.matches(password.getOld(), persistedPerson.getPassword())) {
         throw new PasswordNotAcceptedException();
      }
      return this.personService.updatePassword(persistedPerson, password.getUpdated());
   }

   public boolean register(Person person) {
      Person persistedPerson = this.personService.findByUsername(person.getUsername());
      if (persistedPerson != null) {
         return false;
      }
      if (person.getRole() == null) {
         person.setRole(RoleType.USER);
      }
      person.setPassword(this.encoder.encode(person.getPassword()));
      this.personService.create(person);
      return true;
   }

}
