package org.doctordrue.sharedcosts.business.services.web;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.doctordrue.sharedcosts.business.model.widget.CostViewDto;
import org.doctordrue.sharedcosts.business.model.widget.PaymentDto;
import org.doctordrue.sharedcosts.business.model.widget.StakeDto;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostGroupService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CostService;
import org.doctordrue.sharedcosts.business.services.dataaccess.CurrencyService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PaymentService;
import org.doctordrue.sharedcosts.business.services.dataaccess.PersonService;
import org.doctordrue.sharedcosts.business.services.dataaccess.StakeService;
import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.CostGroup;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 4/1/2022
 **/
@Service
public class CostViewService {

   @Autowired
   private CostService costService;
   @Autowired
   private CostGroupService costGroupService;
   @Autowired
   private PaymentService paymentService;
   @Autowired
   private StakeService stakeService;
   @Autowired
   private CurrencyService currencyService;
   @Autowired
   private PersonService personService;

   public CostViewDto retrieve(Long id) {
      Map<Long, Person> personMap = this.personService.findAll().stream()
              .collect(Collectors.toMap(Person::getId, v -> v));

      Cost cost = this.costService.findById(id);
      Currency currency = this.currencyService.findById(cost.getCurrencyId());
      CostGroup costGroup = this.costGroupService.findById(cost.getGroupId());
      List<PaymentDto> payments = this.paymentService.findAllByCostId(cost.getId()).stream()
              .map(payment -> PaymentDto.from(payment, personMap.get(payment.getPersonId())))
              .collect(Collectors.toList());
      List<StakeDto> stakes = this.stakeService.findAllByCostId(cost.getId()).stream()
              .map(stake -> StakeDto.from(stake, personMap.get(stake.getPersonId())))
              .collect(Collectors.toList());
      List<Person> availableForPaymentPeople = personMap.entrySet().stream()
              .filter(entry -> payments.stream().noneMatch(payment -> Objects.equals(payment.getPerson().getId(), entry.getKey())))
              .map(Map.Entry::getValue)
              .collect(Collectors.toList());
      List<Person> availableForStakePeople = personMap.entrySet().stream()
              .filter(entry -> stakes.stream().noneMatch(stake -> Objects.equals(stake.getPerson().getId(), entry.getKey())))
              .map(Map.Entry::getValue)
              .collect(Collectors.toList());

      return new CostViewDto.Builder(cost)
              .setCurrency(currency)
              .setCostGroup(costGroup)
              .setPayments(payments)
              .setStakes(stakes)
              .setAvailableForStakePeople(availableForStakePeople)
              .setAvailableForPaymentPeople(availableForPaymentPeople)
              .build();
   }

}
