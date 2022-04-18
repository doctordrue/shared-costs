package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.repositories.PersonRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.doctordrue.sharedcosts.exceptions.people.PersonAlreadyExistsException;
import org.doctordrue.sharedcosts.exceptions.people.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
@Secured("ROLE_ADMIN")
public class PersonService implements UserDetailsService, UserDetailsPasswordService {

   @Autowired
   private PersonRepository personRepository;

   @Bean
   public PasswordEncoder encoder() {
      return new BCryptPasswordEncoder();
   }

   public List<Person> findAll() {
      return this.personRepository.findAll();
   }

   public List<Person> findByIds(Iterable<Long> ids) {
      return this.personRepository.findAllById(ids);
   }

   public Person findByEmail(String email) {
      return this.personRepository.findByEmailAllIgnoreCase(email);
   }

   public Person findById(Long id) {
      return this.personRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public Set<Person> findByGroupId(Long id) {
      return this.personRepository.findByGroupsId(id);
   }

   public Person create(Person person) {
      return this.personRepository.save(person);
   }

   public Person update(Long id, Person updatePerson) {
      assumeExists(id);
      Person persistedPerson = this.findById(id);
      if (updatePerson.getPassword() == null) {
         updatePerson.setPassword(persistedPerson.getPassword());
      }
      if (!persistedPerson.getEmail().equals(updatePerson.getEmail()) && this.personRepository.existsByEmailIgnoreCase(updatePerson.getEmail())) {
         throw new PersonAlreadyExistsException(updatePerson.getEmail());
      }
      updatePerson.setId(id);
      return this.personRepository.save(updatePerson);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.personRepository.deleteById(id);
   }

   @Override
   public Person loadUserByUsername(String email) throws UsernameNotFoundException {
      Person person = this.findByEmail(email);
      if (person == null) {
         throw new UsernameNotFoundException("User not found for email = " + email);
      }
      return person;
   }

   @Override
   public Person updatePassword(UserDetails user, String newPassword) {
      Person persistedPerson = this.findByEmail(user.getUsername());
      persistedPerson.setPassword(encoder().encode(newPassword));
      return this.update(persistedPerson.getId(), persistedPerson);
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new PersonNotFoundException(id);
   }

   private void assumeExists(Long id) {
      if (!this.personRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }
}
