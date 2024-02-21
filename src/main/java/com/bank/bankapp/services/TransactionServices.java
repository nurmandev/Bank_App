package com.bank.bankapp.services;

import com.bank.bankapp.dto.TransactionDto;

public interface TransactionServices {
    void saveTransaction(TransactionDto transactionDto);
    
}
