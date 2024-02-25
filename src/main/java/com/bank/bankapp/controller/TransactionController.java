package com.bank.bankapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bankapp.entity.Transaction;
import com.bank.bankapp.services.BankStatement;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@Tag(name = "Bank transaction controller APIs")
@AllArgsConstructor
public class TransactionController {
    
    private BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateStatement(@RequestParam String accountNumber, 
                                               @RequestParam String startDate,
                                               @RequestParam String endDate){
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
