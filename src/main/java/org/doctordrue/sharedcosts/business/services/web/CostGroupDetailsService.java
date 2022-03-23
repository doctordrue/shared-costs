package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.CostDetails;
import org.doctordrue.sharedcosts.business.model.widget.CostGroupDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Service
public class CostGroupDetailsService {
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private CostService costService;
   @Autowired
   private CostDetailsService costDetailsService;


   public CostGroupDetails getDetails(CostGroup group) {
      List<CostDetails> costs = this.costService.findAllByGroupId(group.getId())
              .stream()
              .map(cost -> this.costDetailsService.getDetails(cost))
              .collect(Collectors.toList());
      return new CostGroupDetails().setGroup(group)
              .setCosts(costs);
   }

   public List<CostGroupDetails> getDetails() {
      List<Cost> allCosts = this.costService.findAll();
      return this.costGroupService.findAll().stream()
              .map(group -> new CostGroupDetails().setGroup(group)
                      .setCosts(allCosts.stream()
                              .filter(cost -> cost.getGroupId().equals(group.getId()))
                              .map(cost -> this.costDetailsService.getDetails(cost))
                              .collect(Collectors.toList())))
              .collect(Collectors.toList());
   }
}
