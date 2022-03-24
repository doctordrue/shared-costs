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
      return new ModelAndView("/currency/index", model.asMap());
   }

   @GetMapping("/add")
   public ModelAndView viewAdd(Model model) {
      Currency currency = new Currency().setRate(1d);
      model.addAttribute("currency", currency);
      return new ModelAndView("/currency/add", model.asMap());
   }

   @GetMapping("/{id}/edit")
   public ModelAndView viewEdit(@PathVariable("id") Long id, Model model) {
      Currency currency = this.currencyService.findById(id);
      model.addAttribute("currency", currency);
      return new ModelAndView("/currency/edit", model.asMap());
   }

   @PostMapping("/add/submit")
   public ModelAndView addSubmitAndReturn(@ModelAttribute Currency currency, Model model) {
      this.currencyService.create(currency);
      return this.viewAll(model);
   }

   @PostMapping("/{id}/edit/submit")
   public ModelAndView addSubmitAndReturn(@PathVariable("id") Long id, @ModelAttribute Currency currency, Model model) {
      this.currencyService.updateById(id, currency);
      return this.viewAll(model);
   }

}
