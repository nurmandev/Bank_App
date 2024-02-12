package com.bank.bankapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.bankapp.dto.BankResponse;
import com.bank.bankapp.dto.CreditDebitRequest;
import com.bank.bankapp.dto.EnquiryRequest;
import com.bank.bankapp.dto.UserRequest;
import com.bank.bankapp.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getAccount() {
		return "Hello";
	}

	@PostMapping
	public BankResponse createAccount(@RequestBody  UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@GetMapping("balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request ){
		return userService.balanceEnquiry(request);
	}

	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request){
		return userService.nameEnquiry(request);
	}

	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
		return userService.creditAccount(request);
	}
}
