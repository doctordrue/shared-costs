package org.doctordrue.sharedcosts.controllers.webform;

import java.time.LocalDate;
import java.util.List;

import org.doctordrue.sharedcosts.business.model.widget.CostGroupDetails;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.web.CostGroupDetailsService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
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
 * 3/18/2022
 **/
@Controller
@RequestMapping("/groups")
public class CostGroupWebController {
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private CostGroupDetailsService costGroupDetailsService;

   @GetMapping
   public ModelAndView viewAll(Model model) {
      List<CostGroup> groups = this.costGroupService.findAll();
      model.addAttribute("groups", groups);
      return new ModelAndView("/groups/index", model.asMap());
   }

   @GetMapping("/{id}/edit")
   public ModelAndView viewEdit(@PathVariable("id") Long id, Model model) {
      CostGroup group = this.costGroupService.findById(id);
      model.addAttribute("group", group);
      return new ModelAndView("/groups/edit", model.asMap());
   }

   @GetMapping("/add")
   public ModelAndView viewAdd(Model model) {
      model.addAttribute("group", new CostGroup().setStartDate(LocalDate.now()));
      return new ModelAndView("/groups/add", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("group") CostGroup group) {
      this.costGroupService.create(group);
      return new RedirectView("/groups");
   }

   @GetMapping("/{id}/details")
   public ModelAndView viewDetails(@PathVariable("id") Long groupId, Model model) {
      CostGroup group = this.costGroupService.findById(groupId);
      CostGroupDetails groupDetails = this.costGroupDetailsService.getDetails(group);
      model.addAttribute("costGroupDetails", groupDetails);
      return new ModelAndView("/groups/view", model.asMap());
   }

   @PostMapping("/{id}/edit")
   public RedirectView update(@PathVariable("id") Long id, @ModelAttribute("group") CostGroup group) {
      CostGroup updated = this.costGroupService.update(id, group);
      return new RedirectView("/groups/" + updated.getId() + "/details");
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("id") Long id, @RequestParam("recursively") Boolean recursively) {
      if (!recursively) {
         this.costGroupService.delete(id);
      } else {
         this.costGroupService.deleteRecursively(id);
      }
      return new RedirectView("/groups");
   }

}
