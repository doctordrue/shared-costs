package org.doctordrue.sharedcosts.business.services.dataaccess;

import java.util.List;

import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.repositories.CurrencyRepository;
import org.doctordrue.sharedcosts.exceptions.BaseException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sun.istack.NotNull;

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
      return this.currencyRepository.findOne(Example.of(probe)).orElseThrow(() -> new BaseException("CS001", "Currency not found for short_name = " + currencyType));
   }

   public Currency create(Currency currency) {
      return this.currencyRepository.save(currency);
   }

   public Currency updateById(Long id, Currency newData) {
      if (!this.currencyRepository.existsById(id)) {
         throw new BaseException("CS001", "Currency not found for id = " + id);
      }
      return this.currencyRepository.save(newData);
   }

   public Currency updateByShortName(String shortName, Currency newData) {
      Currency probe = new Currency().setShortName(shortName);
      Currency existingData = this.currencyRepository.findOne(Example.of(probe))
              .orElseThrow(() -> new BaseException("CS001", "Currency not found for short_name = " + shortName));
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
