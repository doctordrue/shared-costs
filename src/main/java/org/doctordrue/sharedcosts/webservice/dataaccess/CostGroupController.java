package org.doctordrue.sharedcosts.webservice.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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

   @GetMapping("/{id}")
   public CostGroup findById(@PathVariable("id") Long id) {
      return this.costGroupService.findById(id);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public CostGroup create(@RequestBody CostGroup costGroup) {
      return this.costGroupService.create(costGroup);
   }

   @PutMapping("/{id}")
   public CostGroup update(@PathVariable("id") Long id, @RequestBody CostGroup costGroup) {
      return this.costGroupService.update(id, costGroup);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("id") Long id) {
      this.costGroupService.delete(id);
   }
}
