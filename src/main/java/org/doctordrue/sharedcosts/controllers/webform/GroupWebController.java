package org.doctordrue.sharedcosts.controllers.webform;

import java.time.LocalDate;
import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.GroupService;
import org.doctordrue.sharedcosts.data.entities.Group;
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
public class GroupWebController {

   @Autowired
   private GroupService groupService;

   @GetMapping
   public ModelAndView viewAll(Model model) {
      List<Group> groups = this.groupService.findAll();
      Group newGroup = new Group().setStartDate(LocalDate.now());
      model.addAttribute("groups", groups);
      model.addAttribute("new_group", newGroup);
      return new ModelAndView("/groups/index", model.asMap());
   }

   @GetMapping("/{id}")
   public ModelAndView view(@PathVariable("id") Long groupId, Model model) {
      Group group = this.groupService.findById(groupId);
      model.addAttribute("group", group);
      return new ModelAndView("/groups/view", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute("group") Group group) {
      this.groupService.create(group);
      return new RedirectView("/groups");
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long id, @ModelAttribute("group") Group group) {
      this.groupService.update(id, group);
      return new RedirectView("/groups");
   }

   @PostMapping("/{id}/delete")
   public RedirectView delete(@PathVariable("id") Long id,
                              @RequestParam(value = "recursively", defaultValue = "false", required = false) Boolean recursively) {
      if (!recursively) {
         this.groupService.delete(id);
      } else {
         this.groupService.deleteRecursively(id);
      }
      return new RedirectView("/groups");
   }

}
