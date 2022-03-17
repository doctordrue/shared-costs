package org.doctordrue.sharedcosts.webservice.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/17/2022
 **/
@RestController
@RequestMapping("/api/v1/cost_groups")
public class CostGroupController {

   @Autowired
   private CostGroupService costGroupService;

   @GetMapping
   public List<CostGroup> findAll() {
      return this.costGroupService.findAll();
   }

}
