package org.doctordrue.sharedcosts.data.repositories;

import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Repository
public interface CostGroupRepository extends JpaRepository<CostGroup, Long> {

}
