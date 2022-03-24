package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@RestController
@RequestMapping("/api/v1/costs")
public class CostController {

   private final CostService costService;

   public CostController(CostService costService) {
      this.costService = costService;
   }

   @GetMapping
   public List<Cost> findAll() {
      return this.costService.findAll();
   }

   @GetMapping(path = "/{id}")
   public Cost findById(@PathVariable Long id) {
      return this.costService.findById(id);
   }

   @GetMapping(params = "group_id")
   public List<Cost> findByGroup(@RequestParam("group_id") Long groupId) {
      return this.costService.findAllByGroupId(groupId);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Cost create(@RequestBody Cost cost) {
      return this.costService.create(cost);
   }

   @PutMapping(path = "/{id}")
   public Cost update(@PathVariable Long id, Cost cost) {
      return this.costService.update(id, cost);
   }

   @DeleteMapping(path = "/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable Long id) {
      this.costService.delete(id);
   }

}
