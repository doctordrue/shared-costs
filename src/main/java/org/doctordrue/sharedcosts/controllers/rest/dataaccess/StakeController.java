package org.doctordrue.sharedcosts.controllers.rest.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.ParticipationService;
import org.doctordrue.sharedcosts.data.entities.Participation;
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

   private final ParticipationService participationService;

   public StakeController(ParticipationService participationService) {
      this.participationService = participationService;
   }

   @GetMapping
   public List<Participation> findAll() {
      return this.participationService.findAll();
   }

   @GetMapping("/{id}")
   public Participation findById(@PathVariable("id") Long id) {
      return this.participationService.findById(id);
   }

   @GetMapping(params = "cost_id")
   public List<Participation> findByCostId(@RequestParam("cost_id") Long costId) {
      return this.participationService.findAllByCostId(costId);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Participation create(@RequestBody Participation participation) {
      return this.participationService.create(participation);
   }

   @PutMapping("/{id}")
   public Participation update(@PathVariable("id") Long id, @RequestBody Participation participation) {
      return this.participationService.update(id, participation);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("id") Long id) {
      this.participationService.delete(id);
   }
}
