package org.doctordrue.sharedcosts.data.repositories;

import java.util.Optional;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

   Optional<Currency> findByShortNameIgnoreCase(String shortName);

}