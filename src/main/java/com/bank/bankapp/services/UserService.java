package com.bank.bankapp.services;

import com.bank.bankapp.dto.BankResponse;
import com.bank.bankapp.dto.UserRequest;


public interface UserService {
	BankResponse createAccount(UserRequest userRequest);
}
