package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.repositories.PersonRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class PersonService {

   private final PersonRepository personRepository;

   public PersonService(PersonRepository personRepository) {
      this.personRepository = personRepository;
   }

   public List<Person> findAll() {
      return this.personRepository.findAll();
   }

   public List<Person> findByIds(Iterable<Long> ids) {
      return this.personRepository.findAllById(ids);
   }

   public Person findById(Long id) {
      return this.personRepository.findById(id).orElseThrow(() -> generateNotFoundByIdException(id));
   }

   public Person create(Person person) {
      return this.personRepository.save(person);
   }

   public Person update(Long id, Person person) {
      assumeExists(id);
      person.setId(id);
      return this.personRepository.save(person);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.personRepository.deleteById(id);
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
