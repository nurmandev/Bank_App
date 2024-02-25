package com.bank.bankapp.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.repository.TransactionRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BankStatement {
    private TransactionRepository transactionRepository;
   //  private static final String FILE = "C:\\User\\Admin\\Documents\\MyStatement.pdf" ;
   
    /**
     * retrive list of Transaction within a date range given an account number
     * generate a pdf file of transactions
     * send the file via email
     */
    
     public List<Transaction>generateStatement(String accountNumber, String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream()
        .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
        .filter(transaction -> transaction.getCreatedAt().isEqual(start))
        .filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

      return transactionList;

     }

   //   public void designStatement(List<Transaction>transactions) {

   // }
}
