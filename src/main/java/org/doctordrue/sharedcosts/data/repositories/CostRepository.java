package org.doctordrue.sharedcosts.data.repositories;

import java.util.List;
import java.util.Optional;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostRepository extends JpaRepository<Cost, Long> {

   List<Cost> findByGroupId(Long id);

   Optional<Cost> findByName(String name);

   boolean existsByName(String name);
}