package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

   @Autowired
   private PersonService personService;

   @GetMapping
   public List<Person> findAll() {
      return this.personService.findAll();
   }

   @GetMapping("/{id}")
   public Person findById(@PathVariable("id") Long id) {
      return this.personService.findById(id);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Person create(@RequestBody Person person) {
      return this.personService.create(person);
   }

   @PutMapping("/{id}")
   public Person update(@PathVariable("id") Long id, @RequestBody Person person) {
      return this.personService.update(id, person);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable Long id) {
      this.personService.delete(id);
   }

}
