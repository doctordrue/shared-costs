package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
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
   private GroupService groupService;

   @GetMapping
   public List<Group> findAll() {
      return this.groupService.findAll();
   }

   @GetMapping("/{id}")
   public Group findById(@PathVariable("id") Long id) {
      return this.groupService.findById(id);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Group create(@RequestBody Group group) {
      return this.groupService.create(group);
   }

   @PutMapping("/{id}")
   public Group update(@PathVariable("id") Long id, @RequestBody Group group) {
      return this.groupService.update(id, group);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("id") Long id) {
      this.groupService.delete(id);
   }
}
