package org.doctordrue.sharedcosts.telegram.data.entities;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.doctordrue.sharedcosts.data.entities.Cost;
import org.doctordrue.sharedcosts.data.entities.Currency;
import org.doctordrue.sharedcosts.data.entities.Group;
import org.doctordrue.sharedcosts.data.entities.Participation;
import org.doctordrue.sharedcosts.data.entities.Person;
import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.telegram.session.userchat.UserChatState;
import org.doctordrue.telegram.bot.api.session.IBotSession;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Entity
@Table(name = "user_chat_session")
public class UserChatSession implements IBotSession<UserChatState> {

   @Id
   @Column(name = "chat_id", nullable = false)
   private Long chatId;

   @Column(name = "state", nullable = false)
   @Enumerated(EnumType.STRING)
   private UserChatState state = UserChatState.BEFORE_START;

   @ManyToOne(optional = true)
   @JoinColumn(name = "selected_group_id")
   private Group selectedGroup;

   @ManyToOne(optional = true)
   @JoinColumn(name = "currency_id")
   private Currency currency;

   @Column(name = "temp_cost_name")
   private String tempCostName;

   @ManyToOne(optional = true)
   @JoinColumn(name = "selected_cost_id")
   private Cost selectedCost;

   @Column(name = "temp_payment_amount")
   private Double tempPaymentAmount;

   @Column(name = "temp_participation_name")
   private String tempParticipationName;

   @Column(name = "temp_participation_amount")
   private Double tempParticipationAmount;

   @Column(name = "temp_transaction_amount")
   private Double tempTransactionAmount;

   @ManyToOne(optional = true)
   @JoinColumn(name = "selected_transaction_id", nullable = true)
   private Transaction selectedTransaction;

   @ManyToOne
   @JoinColumn(name = "selected_participation_id")
   private Participation selectedParticipation;

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "user_chat_session_currencies")
   private Set<Currency> availableCurrencies = new LinkedHashSet<>();

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(name = "user_chat_session_groups")
   private Set<Group> availableGroups = new LinkedHashSet<>();

   @ManyToOne
   @JoinColumn(name = "temp_cost_payer_id")
   private Person tempCostPayer;

   public Person getTempCostPayer() {
      return tempCostPayer;
   }

   public UserChatSession setTempCostPayer(Person tempCostPayer) {
      this.tempCostPayer = tempCostPayer;
      return this;
   }

   public Participation getSelectedParticipation() {
      return selectedParticipation;
   }

   public UserChatSession setSelectedParticipation(Participation selectedParticipation) {
      this.selectedParticipation = selectedParticipation;
      return this;
   }

   public Set<Group> getAvailableGroups() {
      return availableGroups;
   }

   public UserChatSession setAvailableGroups(Set<Group> availableGroups) {
      this.availableGroups = availableGroups;
      return this;
   }

   public Set<Currency> getAvailableCurrencies() {
      return availableCurrencies;
   }

   public UserChatSession setAvailableCurrencies(Set<Currency> availableCurrencies) {
      this.availableCurrencies = availableCurrencies;
      return this;
   }

   public Transaction getSelectedTransaction() {
      return selectedTransaction;
   }

   public UserChatSession setSelectedTransaction(Transaction selectedTransaction) {
      this.selectedTransaction = selectedTransaction;
      return this;
   }

   public Double getTempTransactionAmount() {
      return tempTransactionAmount;
   }

   public UserChatSession setTempTransactionAmount(Double tempTransactionAmount) {
      this.tempTransactionAmount = tempTransactionAmount;
      return this;
   }

   public Double getTempParticipationAmount() {
      return tempParticipationAmount;
   }

   public UserChatSession setTempParticipationAmount(Double tempParticipationAmount) {
      this.tempParticipationAmount = tempParticipationAmount;
      return this;
   }

   public String getTempParticipationName() {
      return tempParticipationName;
   }

   public UserChatSession setTempParticipationName(String tempParticipationName) {
      this.tempParticipationName = tempParticipationName;
      return this;
   }

   public Double getTempPaymentAmount() {
      return tempPaymentAmount;
   }

   public UserChatSession setTempPaymentAmount(Double tempPaymentAmount) {
      this.tempPaymentAmount = tempPaymentAmount;
      return this;
   }

   public Cost getSelectedCost() {
      return selectedCost;
   }

   public UserChatSession setSelectedCost(Cost selectedCost) {
      this.selectedCost = selectedCost;
      return this;
   }

   public Currency getCurrency() {
      return currency;
   }

   public UserChatSession setCurrency(Currency currency) {
      this.currency = currency;
      return this;
   }

   public String getTempCostName() {
      return tempCostName;
   }

   public UserChatSession setTempCostName(String tempCostName) {
      this.tempCostName = tempCostName;
      return this;
   }

   public Group getSelectedGroup() {
      return selectedGroup;
   }

   public UserChatSession setSelectedGroup(Group selectedGroup) {
      this.selectedGroup = selectedGroup;
      return this;
   }

   public Long getChatId() {
      return chatId;
   }

   public UserChatSession setChatId(Long chatId) {
      this.chatId = chatId;
      return this;
   }

   public UserChatState getState() {
      return state;
   }

   public UserChatSession setState(UserChatState state) {
      this.state = state;
      return this;
   }

   public static UserChatSession newSession(Chat chat) {
      return new UserChatSession().setChatId(chat.getId()).setState(UserChatState.BEFORE_START);
   }
}