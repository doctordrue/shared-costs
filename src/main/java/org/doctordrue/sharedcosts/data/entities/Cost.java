package org.doctordrue.sharedcosts.data.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.business.model.debt_calculation.Total;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "costs")
public class Cost {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "total", nullable = false)
   private Double total;

   @Column(name = "datetime", nullable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
   private LocalDateTime datetime;

   @ManyToOne(optional = false)
   private Group group;

   @ManyToOne(optional = false)
   private Currency currency;

   @OneToMany(mappedBy = "cost", fetch = FetchType.EAGER)
   @OrderBy("id")
   private Set<Payment> payments;

   @OneToMany(mappedBy = "cost", fetch = FetchType.EAGER)
   @OrderBy("id")
   private Set<Participation> participations;

   public Long getId() {
      return id;
   }

   public Cost setId(Long id) {
      this.id = id;
      return this;
   }

   public String getName() {
      return name;
   }

   public Cost setName(String name) {
      this.name = name;
      return this;
   }

   public Group getGroup() {
      return group;
   }

   public Cost setGroup(Group group) {
      this.group = group;
      return this;
   }

   public Currency getCurrency() {
      return currency;
   }

   public Cost setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public Double getTotal() {
      return total;
   }

   public Cost setTotal(Double total) {
      this.total = total;
      return this;
   }

   public LocalDateTime getDatetime() {
      return datetime;
   }

   public Cost setDatetime(LocalDateTime datetime) {
      this.datetime = datetime;
      return this;
   }

   public Set<Payment> getPayments() {
      return payments;
   }

   public Cost setPayments(Set<Payment> payments) {
      this.payments = payments;
      return this;
   }

   public Set<Participation> getParticipations() {
      return participations;
   }

   public Cost setParticipations(Set<Participation> participations) {
      this.participations = participations;
      return this;
   }

   public List<Total> getParticipationsTotals() {
      return this.getParticipations().stream().flatMap(p -> p.getFlatAmounts().stream()).collect(Collectors.toList());
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;

      Cost cost = (Cost) o;

      return getId().equals(cost.getId());
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   @Override
   public String toString() {
      return new StringJoiner(", ", Cost.class.getSimpleName() + "[", "]")
              .add("id=" + id)
              .add("name='" + name + "'")
              .add("datetime=" + datetime)
              .toString();
   }

   public String toTelegramString() {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("_'%1$s'_ от *%2$td %2$tB %2$tY*:\n", this.getName(), this.getDatetime()))
              .append("Сумма: ").append(String.format("%.2f ", this.getTotal())).append(this.getCurrency().getShortName()).append("\n");
      if (!this.getPayments().isEmpty()) {
         sb.append("Оплачено:\n");
         this.getPayments().forEach(p -> sb.append(String.format("%.2f ", p.getAmount())).append(p.getCurrency().getShortName()).append(" - ").append(p.getPerson().getFullName()));
      }
      return sb.toString();
   }
}
