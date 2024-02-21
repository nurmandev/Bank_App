package com.bank.bankapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.bankapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

    
}