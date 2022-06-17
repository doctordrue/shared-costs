package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import org.telegram.telegrambots.meta.api.objects.User;

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

   public Person findByUsername(String email) {
      return this.personRepository.findByUsernameAllIgnoreCase(email);
   }

   public Person findById(Long id) {
      return this.personRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public Optional<Person> findByTelegramId(Long telegramId) {
      return this.personRepository.findByTelegramId(telegramId);
   }

   public Optional<Person> find(String username) {
      return this.personRepository.findByUsername(username);
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
      if (!persistedPerson.getUsername().equals(updatePerson.getUsername()) && this.personRepository.existsByUsernameIgnoreCase(updatePerson.getUsername())) {
         throw new PersonAlreadyExistsException(updatePerson.getUsername());
      }
      updatePerson.setId(id);
      return this.personRepository.save(updatePerson);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.personRepository.deleteById(id);
   }

   @Override
   public Person loadUserByUsername(String username) throws UsernameNotFoundException {
      Person person = this.findByUsername(username);
      if (person == null) {
         throw new UsernameNotFoundException("User not found for username = " + username);
      }
      return person;
   }

   @Override
   public Person updatePassword(UserDetails user, String newPassword) {
      Person persistedPerson = this.findByUsername(user.getUsername());
      persistedPerson.setPassword(encoder().encode(newPassword));
      return this.update(persistedPerson.getId(), persistedPerson);
   }

   public Person addTelegramUser(User user) {
      String identity = StringUtils.isEmpty(user.getUserName()) ? user.getId().toString() : user.getUserName();
      Person persistedPerson = this.findByUsername(identity);
      if (persistedPerson != null) {
         if (persistedPerson.hasTelegramId()) {
            throw new PersonAlreadyExistsException(identity);
         }
         persistedPerson.setTelegramId(user.getId());
      } else {
         persistedPerson = Person.fromTelegram(user);
      }
      return this.personRepository.save(persistedPerson);
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
