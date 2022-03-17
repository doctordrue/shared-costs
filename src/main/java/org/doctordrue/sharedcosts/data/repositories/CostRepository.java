package org.doctordrue.sharedcosts.data.repositories;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {

}