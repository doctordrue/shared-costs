package org.doctordrue.sharedcosts.controllers.webform;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Andrey_Barantsev
 * 3/24/2022
 **/
@Controller
@RequestMapping("/currency")
public class CurrencyWebController {
   @Autowired
   private CurrencyService currencyService;

   @GetMapping
   public ModelAndView viewAll(Model model) {
      List<Currency> currencies = this.currencyService.findAll();
      model.addAttribute("currencies", currencies);
      model.addAttribute("new_currency", new Currency());
      return new ModelAndView("/currency/index", model.asMap());
   }

   @PostMapping("/add")
   public RedirectView add(@ModelAttribute Currency currency) {
      this.currencyService.create(currency);
      return new RedirectView("/currency");
   }

   @PostMapping("/{id}/edit")
   public RedirectView edit(@PathVariable("id") Long id, @ModelAttribute Currency currency) {
      this.currencyService.updateById(id, currency);
      return new RedirectView("/currency");
   }

}
