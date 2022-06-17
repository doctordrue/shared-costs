package org.doctordrue.sharedcosts.data.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author Andrey_Barantsev
 * 3/16/2022
 **/
@Entity
@Table(name = "groups")
public class Group {

   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false)
   private String name;

   @Column(name = "description")
   private String description;

   @Column(name = "start_date", nullable = false)
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate startDate;

   @Column(name = "end_date")
   @DateTimeFormat(pattern = "yyyy-MM-dd")
   private LocalDate endDate;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
           name = "groups_participants",
           joinColumns = @JoinColumn(
                   name = "group_id",
                   referencedColumnName = "id"))
   private Set<Person> participants = new LinkedHashSet<>();

   @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
   @OrderBy("datetime DESC")
   private final Set<Cost> costs = new HashSet<>();

   @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
   private final Set<Transaction> transactions = new HashSet<>();

   public Long getId() {
      return id;
   }

   public Group setId(Long id) {
      this.id = id;
      return this;
   }

   public String getName() {
      return name;
   }

   public Group setName(String name) {
      this.name = name;
      return this;
   }

   public String getDescription() {
      return description;
   }

   public Group setDescription(String description) {
      this.description = description;
      return this;
   }

   public LocalDate getStartDate() {
      return startDate;
   }

   public Group setStartDate(LocalDate startDate) {
      this.startDate = startDate;
      return this;
   }

   public LocalDate getEndDate() {
      return endDate;
   }

   public Group setEndDate(LocalDate endDate) {
      this.endDate = endDate;
      return this;
   }

   public Set<Person> getParticipants() {
      return participants;
   }

   public Group setParticipants(Set<Person> participants) {
      this.participants = participants;
      return this;
   }

   public Set<Cost> getCosts() {
      return costs;
   }

   public Set<Transaction> getTransactions() {
      return transactions;
   }

   public boolean isParticipated(String username) {
      return this.participants.stream().anyMatch(p -> p.getUsername().equals(username));
   }
}
