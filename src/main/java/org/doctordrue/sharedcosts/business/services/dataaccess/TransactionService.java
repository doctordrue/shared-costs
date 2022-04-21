package org.doctordrue.sharedcosts.business.services.dataaccess;

import org.doctordrue.sharedcosts.data.entities.Transaction;
import org.doctordrue.sharedcosts.data.repositories.TransactionRepository;
import org.doctordrue.sharedcosts.exceptions.transaction.TransactionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andrey_Barantsev
 * 4/21/2022
 **/
@Service
public class TransactionService {

   @Autowired
   private TransactionRepository transactionRepository;

   public Transaction findById(Long id) {
      assumeExists(id);
      return this.transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
   }

   public Transaction create(Transaction transaction) {
      return this.transactionRepository.save(transaction);
   }

   public Transaction update(Long id, Transaction updateTransaction) {
      Transaction persistedTransaction = this.findById(id);
      updateTransaction.setId(persistedTransaction.getId());
      return this.transactionRepository.save(updateTransaction);
   }

   public void delete(Long id) {
      assumeExists(id);
      this.transactionRepository.deleteById(id);
   }

   private void assumeExists(Long id) {
      if (!this.transactionRepository.existsById(id)) {
         throw new TransactionNotFoundException(id);
      }
   }
}
