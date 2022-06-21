package org.doctordrue.sharedcosts.controllers.webform;

import java.util.Set;

import org.doctordrue.sharedcosts.business.model.webform.processing.SplitInputData;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.processing.ParticipationProcessingService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/22/2022
 **/
@Controller
@RequestMapping("/costs/{cost_id}/participation")
public class ParticipationWebController {

   @Autowired
   private CostService costService;
   @Autowired
   private ParticipationProcessingService participationProcessingService;

   @GetMapping("/split")
   public ModelAndView viewSplit(@PathVariable("cost_id") Long costId,
                                 @RequestParam("amount") Double amount,
                                 Model model) {
      Cost cost = this.costService.findById(costId);
      model.addAttribute("cost", cost);
      model.addAttribute("split", new SplitInputData().setAmount(amount));
      return new ModelAndView("/costs/participation/split", model.asMap());
   }

   @PostMapping("/split")
   public RedirectView addSplit(@PathVariable("cost_id") Long costId,
                                @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") boolean updateCost,
                                @ModelAttribute("split") SplitInputData split) {
      Cost cost = this.costService.findById(costId);
      int size = split.getPeople().size();
      String name = split.getName();
      double amount = split.getAmount();
      Set<Person> people = split.getPeople();
      Participation participation = new Participation().setName(name)
              .setAmount(amount)
              .setPeople(people)
              .setCost(cost);
      this.participationProcessingService.processNew(participation, updateCost);
      return new RedirectView("/costs/" + costId);
   }

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
                            @RequestParam(value = "recalculate_cost", required = false, defaultValue = "false") boolean updateCost,
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
