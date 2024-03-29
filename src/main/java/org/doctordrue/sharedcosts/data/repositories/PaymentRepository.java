package org.doctordrue.sharedcosts.data.repositories;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

   List<Payment> findByCostId(Long id);
}