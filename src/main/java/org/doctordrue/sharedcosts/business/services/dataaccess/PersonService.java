package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.enums.RoleType;
import org.doctordrue.sharedcosts.data.repositories.PersonRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

   public boolean register(Person person) {
      Person persistedPerson = this.findByEmail(person.getEmail());
      if (persistedPerson != null) {
         return false;
      }
      if (person.getRole() == null) {
         person.setRole(RoleType.USER);
      }
      person.setPassword(encoder().encode(person.getPassword()));
      this.create(person);
      return true;
   }

   public Person update(Long id, Person person) {
      assumeExists(id);
      if (person.getPassword() == null) {
         Person persistedPerson = this.findById(id);
         person.setPassword(persistedPerson.getPassword());
      }
      person.setId(id);
      return this.personRepository.save(person);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.personRepository.deleteById(id);
   }

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Person person = this.findByEmail(email);
      if (person == null) {
         throw new UsernameNotFoundException("User not found for email = " + email);
      }
      return person;
   }

   @Override
   public UserDetails updatePassword(UserDetails user, String newPassword) {
      Person persistedPerson = this.findByEmail(user.getUsername());
      persistedPerson.setPassword(encoder().encode(newPassword));
      return this.update(persistedPerson.getId(), persistedPerson);
   }

   private BaseException generateNotFoundByIdException(Long id) {
      return new BaseException("SC005", "Person not found for id = " + id);
   }

   private void assumeExists(Long id) {
      if (!this.personRepository.existsById(id)) {
         throw generateNotFoundByIdException(id);
      }
   }
}
