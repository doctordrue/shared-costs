package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;
import java.util.Optional;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.repositories.CurrencyRepository;
import org.doctordrue.sharedcosts.exceptions.currency.CurrencyNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Service
public class CurrencyService {

   private final CurrencyRepository currencyRepository;

   public CurrencyService(CurrencyRepository currencyRepository) {
      this.currencyRepository = currencyRepository;
   }

   public List<Currency> findAll() {
      return this.currencyRepository.findAll(Sort.by("id"));
   }

   public Currency findById(Long id) {
      return this.currencyRepository.getById(id);
   }

   public Currency findByShortName(String currencyType) {
      Currency probe = new Currency();
      probe.setShortName(currencyType);
      return this.currencyRepository.findOne(Example.of(probe)).orElseThrow(() -> new CurrencyNotFoundException(currencyType));
   }

   public Optional<Currency> find(String shortName) {
      return this.currencyRepository.findByShortNameIgnoreCase(shortName);
   }

   public Currency create(Currency currency) {
      return this.currencyRepository.save(currency);
   }

   public Currency updateById(Long id, Currency newData) {
      if (!this.currencyRepository.existsById(id)) {
         throw new CurrencyNotFoundException(id);
      }
      return this.currencyRepository.save(newData);
   }

   public Currency updateByShortName(String shortName, Currency newData) {
      Currency probe = new Currency().setShortName(shortName);
      Currency existingData = this.currencyRepository.findOne(Example.of(probe))
              .orElseThrow(() -> new CurrencyNotFoundException(shortName));
      newData.setId(existingData.getId());
      if (newData.getShortName() == null) {
         newData.setShortName(shortName);
      }
      if (newData.getFullName() == null) {
         newData.setFullName(existingData.getFullName());
      }
      return this.currencyRepository.save(newData);
   }

   public void delete(String shortName) {
      Long id = this.findByShortName(shortName)
              .getId();
      this.currencyRepository.deleteById(id);
   }
}
