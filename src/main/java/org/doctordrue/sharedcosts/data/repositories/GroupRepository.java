package org.doctordrue.sharedcosts.data.repositories;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

   List<Group> findByParticipantsId(Long id);
}
