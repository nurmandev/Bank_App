package com.bank.bankapp.dto;

import java.math.BigDecimal;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private String sourceAccountNumber;
    private String destinationAccountNumer;
    private BigDecimal amount;

}
