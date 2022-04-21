package org.doctordrue.sharedcosts.data.repositories;

import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}