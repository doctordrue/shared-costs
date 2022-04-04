package org.doctordrue.sharedcosts.data.repositories;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

   List<Participation> findByCostId(Long id);
}