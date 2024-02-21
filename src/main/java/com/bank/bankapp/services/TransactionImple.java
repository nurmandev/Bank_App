package com.bank.bankapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.bankapp.dto.TransactionDto;
import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.repository.TransactionRepository;

@Component
public class TransactionImple implements TransactionServices {

    @Autowired
    TransactionRepository transactionRepository;
    
    @Override
    public void saveTransaction(TransactionDto transactionDto){
        Transaction transaction = Transaction.builder()
        .transactionType(transactionDto.getTransactionType())
        .accountNumber(transactionDto.getAccountNumber())
        .amount(transactionDto.getAmount())
        .status("SUCCESS")
        .build();

        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }
}
