package org.doctordrue.sharedcosts.webservice.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.data.entities.Currency;
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
 * 3/16/2022
 **/
@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {

   private final CurrencyService currencyService;

   public CurrencyController(CurrencyService currencyService) {
      this.currencyService = currencyService;
   }

   @GetMapping
   public List<Currency> findAll() {
      return this.currencyService.findAll();
   }

   @GetMapping(path = "/{short_name}")
   public Currency findByShortName(@PathVariable("short_name") String shortName) {
      return this.currencyService.findByShortName(shortName);
   }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public Currency create(@RequestBody Currency currency) {
      return this.currencyService.create(currency);
   }

   @PutMapping(path = "/{short_name}")
   public Currency update(@PathVariable("short_name") String shortName, @RequestBody Currency currency) {
      return this.currencyService.updateByShortName(shortName, currency);
   }

   @DeleteMapping(path = "/{short_name}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void delete(@PathVariable("short_name") String shortName) {
      this.currencyService.delete(shortName);
   }
}
