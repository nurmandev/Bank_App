package com.bank.bankapp.utils;

import java.time.Year;

public class AccountUtils {

	public static final String ACCOUNT_EXISTS_CODE = "001";
	public static final String ACCOUNT_EXISTS_MESSAGE = "This user is already has an account Created";
	
	
	public static final String ACCOUNT_CREATION_SUCCESS = "002";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully Created";
	
	public static final String ACCOUNT_CREDIT_SUCCESS = "003";
	public static final String ACCOUNT_CREDIT_MESSAGE = "Transfer has been successfully";
	
	public static final String ACCOUNT_NOT_EXISTS_CODE = "005";
	public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with provided Account number doesn't exist";
	
	public static final String INSUFFICIENT_BALANCE_CODE = "006";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";
	
	public static final String ACCOUNT_DEBIT_SUCCESS = "007";
	public static final String ACCOUNT_DEBIT_MESSAGE = "Account has been successfully debited";
	
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_SUCCESS = "Account found successfully";
	
	public static String generateAccountNumber() {
		/**
		 * 2024 + randomSixDigits
		 */
		Year currentYear = Year.now();
		int min = 100000;
		int max = 999999;

		//Generate a random number between min and max
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
		//convert the current and randomNumber to Strings, then concatenate them together


		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		StringBuilder accountNumber = new StringBuilder();


		return accountNumber.append(year).append(randomNumber).toString();
	}
}
