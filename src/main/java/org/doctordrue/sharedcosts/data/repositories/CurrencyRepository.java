package org.doctordrue.sharedcosts.data.repositories;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

}