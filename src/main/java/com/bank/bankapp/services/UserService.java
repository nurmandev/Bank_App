package com.bank.bankapp.services;

import com.bank.bankapp.dto.BankResponse;
import com.bank.bankapp.dto.CreditDebitRequest;
import com.bank.bankapp.dto.EnquiryRequest;
import com.bank.bankapp.dto.TransferRequest;
import com.bank.bankapp.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	String nameEnquiry(EnquiryRequest request);
	BankResponse creditAccount(CreditDebitRequest request);
	BankResponse debitAccount(CreditDebitRequest request);
	BankResponse transfer(TransferRequest request);
}
