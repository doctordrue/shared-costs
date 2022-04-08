package org.doctordrue.sharedcosts.controllers.webform;

import org.doctordrue.sharedcosts.business.services.processing.ParticipationProcessingService;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Controller
@RequestMapping("/costs/{cost_id}/participation")
public class ParticipationWebController {

   @Autowired
   private ParticipationProcessingService participationProcessingService;

   @PostMapping("/add")
   public RedirectView add(@PathVariable("cost_id") Long costId,
                           @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") boolean updateCost,
                           @ModelAttribute("participation") Participation participation) {
      this.participationProcessingService.processNew(participation, updateCost);
      return new RedirectView("/costs/" + costId);
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("cost_id") Long costId,
                            @PathVariable("id") Long id,
                            @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") Boolean updateCost,
                            @ModelAttribute("participation") Participation participation) {
      this.participationProcessingService.processEdit(participation, updateCost);
      return new RedirectView("/costs/" + costId);
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("cost_id") Long costId,
                              @PathVariable("id") Long id,
                              @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") Boolean updateCost) {
      this.participationProcessingService.processDelete(id, updateCost);
      return new RedirectView("/costs/" + costId);
   }
}
