package org.doctordrue.sharedcosts.web;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.xpath.internal.operations.Mod;

/**
 * @author Andrey_Barantsev
 * 3/18/2022
 **/
@Controller
@RequestMapping("/")
public class CostGroupsController {

   private final CostGroupService costGroupService;

   public CostGroupsController(CostGroupService costGroupService) {
      this.costGroupService = costGroupService;
   }

   @GetMapping
   public ModelAndView getAllCostGroups(Model model){
      List<CostGroup> groups = this.costGroupService.findAll();
      model.addAttribute("costGroups", groups);
      return new ModelAndView("index", model.asMap());
   }

   @GetMapping("/groups/edit/{id}")
   public ModelAndView viewCostGroup(@PathVariable Long id, Model model) {
      CostGroup group = this.costGroupService.findById(id);
      model.addAttribute("costGroup", group);
      return new ModelAndView("groups/edit", model.asMap());
   }

}
