package org.doctordrue.sharedcosts.data.repositories;

import java.util.Optional;
import java.util.Set;

import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
   Person findByUsernameAllIgnoreCase(String username);
   boolean existsByUsernameIgnoreCase(String username);
   Set<Person> findByGroupsId(Long id);
   Optional<Person> findByTelegramId(Long telegramId);

   Optional<Person> findByUsername(String username);


}
