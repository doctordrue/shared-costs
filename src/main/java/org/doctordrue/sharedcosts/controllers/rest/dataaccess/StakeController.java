package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Stake;
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
 * 3/18/2022
 **/
@RestController
@RequestMapping("/api/v1/stakes")
public class StakeController {

   private final StakeService stakeService;

   public StakeController(StakeService stakeService) {
      this.stakeService = stakeService;
   }

   @GetMapping
   public List<Stake> findAll() {
      return this.stakeService.findAll();
   }

   @GetMapping("/{id}")
   public Stake findById(@PathVariable("id") Long id) {
      return this.stakeService.findById(id);
   }

   @GetMapping(params = "cost_id")
   public List<Stake> findByCostId(@RequestParam("cost_id") Long costId) {
      return this.stakeService.findAllByCostId(costId);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Stake create(@RequestBody Stake stake) {
      return this.stakeService.create(stake);
   }

   @PutMapping("/{id}")
   public Stake update(@PathVariable("id") Long id, @RequestBody Stake stake) {
      return this.stakeService.update(id, stake);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("id") Long id) {
      this.stakeService.delete(id);
   }
}
