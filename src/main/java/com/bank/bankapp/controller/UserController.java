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
import com.bank.bankapp.dto.GetAllUsers;
import com.bank.bankapp.dto.LoginDto;
import com.bank.bankapp.dto.TransferRequest;
import com.bank.bankapp.dto.UserRequest;
import com.bank.bankapp.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user controller Management APIs")


public class UserController {
	@Autowired
	UserService userService;

	@Operation(
		summary = "Hello world",
		description = "Check if server is running"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200"
	)
	@GetMapping
	public String getAccount() {
		return "Hello";
	}

	// @Operation(
	// 	summary = "Hello world",
	// 	description = "Check if server is running"
	// )
	// @ApiResponse(
	// 	responseCode = "200",
	// 	description = "Http service 200"
	// )
	// @GetMapping("/getAllUsers")
	// public BankResponse <List<UserRequest>> getAllUser( ) {
	// 	return new userService.getAllUsers();
	// }


	@Operation(
		summary = "Create New user Account",
		description = "Creating a new user Account and assigning account ID"
	)
	@ApiResponse(
		responseCode = "201",
		description = "Http service 201 CREATED"
	)
	@PostMapping("/register")
	public BankResponse createAccount(@RequestBody  UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}

	@Operation(
		summary = "Login user Account",
		description = "Login to user Account"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@PostMapping("/login")
	public BankResponse login(@RequestBody LoginDto loginDto){
		return userService.login(loginDto);
	}

	@Operation(
		summary = "Balance Enquiry",
		description = "Given an Account number and Check how much the user has"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@GetMapping("balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request ){
		return userService.balanceEnquiry(request);
	}

	@Operation(
		summary = "Name Enquiry",
		description = "use an Account number to check the user name"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest request){
		return userService.nameEnquiry(request);
	}

	@Operation(
		summary = "Credit",
		description = "Deposit money to user Account number"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@PostMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
		return userService.creditAccount(request);
	}

	@Operation(
		summary = "Debit",
		description = "Given an Account number to debit the user"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@PostMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
		return userService.debitAccount(request);
	}

	@Operation(
		summary = "Transfer",
		description = "Transfer money to another user, using an account number"
	)
	@ApiResponse(
		responseCode = "200",
		description = "Http service 200 SUCCESS"
	)
	@PostMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest request){
		return userService.transfer(request);
	}
}
