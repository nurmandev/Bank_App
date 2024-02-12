package com.bank.bankapp.services;

import java.math.BigDecimal;


// import com.bank.bankapp.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.bankapp.dto.AccountInfo;
import com.bank.bankapp.dto.BankResponse;
import com.bank.bankapp.dto.CreditDebitRequest;
import com.bank.bankapp.dto.EnquiryRequest;
import com.bank.bankapp.dto.UserRequest;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.UserRepository; 
import com.bank.bankapp.utils.AccountUtils;


@Service
public class UserServiceImplementation  implements UserService  {

	@Autowired
	UserRepository userRepository;

	// @Autowired
	// EmailService emailService;

    
	 @Override
	    public BankResponse createAccount(UserRequest userRequest) {
	        /**
	         * Creating an account - saving a new user into the db
	         * check if user already has an account
	         */
		 if (userRepository.existsByEmail(userRequest.getEmail())) {
			 BankResponse bankResponse = new BankResponse();
			 bankResponse.setResponseCode(AccountUtils.ACCOUNT_EXISTS_CODE);
			 bankResponse.setResponseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE);
			 bankResponse.setAccountInfo(null);
			 return bankResponse;
		 }


		 User newUser = User.builder()
				 .firstName(userRequest.getFirstName())
				 .lastName(userRequest.getLastName())
				 .otherName(userRequest.getOtherName())
				 .gender(userRequest.getGender())
				 .address(userRequest.getAddress())
				 .stateOfOrigin(userRequest.getStateOfOrigin())
				 .accountNumber(AccountUtils.generateAccountNumber())
				 .accountBalance(BigDecimal.ZERO)
				 .email(userRequest.getEmail())
				 .phoneNumber(userRequest.getPhoneNumber())
				 .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				 .status("ACTIVE")
				 .build();


		 // Save the new user to the database
	        User savedUser = userRepository.save(newUser);

//			Email Service Alert


		//  EmailDetails emailDetails = EmailDetails.builder()
		// 		 .recipient(savedUser.getEmail())
		// 		 .subject("ACCOUNT CREATION")
		// 		 .messageBody("Congratulation! your Account has been Successfully Created."
		// 		 )
		// 		 .build();

		//  emailService.sendEmailAlert(emailDetails);

//		 Return a response

		 return BankResponse.builder()
				 .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				 .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				 .accountInfo(AccountInfo.builder()
						 .accountBalance(savedUser.getAccountBalance())
						 .accountNumber(savedUser.getAccountNumber())
						 .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
						 .build())
				 .build();
	 }

	 @Override
	 public BankResponse balanceEnquiry(EnquiryRequest request){
		// Check if provided account number exists in the database
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
			.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			.accountInfo(null).build();
		}

		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return BankResponse.builder()
		.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
		.responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
		.accountInfo(AccountInfo.builder()
		.accountBalance(foundUser.getAccountBalance())
		.accountName(foundUser.getFirstName() + 
		" " + foundUser.getLastName())
		.build())
		.build();
	 }

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		// Check name exists in the database
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
		}
		User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
		return foundUser.getFirstName() + " " + foundUser.getLastName();
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		// checking if the account exist
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
			.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			.accountInfo(null).build();
		}
		User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepository.save(userToCredit);
		return BankResponse.builder()
		.responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
		.responseMessage(AccountUtils.ACCOUNT_CREDIT_MESSAGE)
		.accountInfo(AccountInfo.builder()
		.accountName(userToCredit.getFirstName() + 
		" " + userToCredit.getLastName())
		.accountBalance(userToCredit.getAccountBalance())
		.accountNumber(request.getAccountNumber())
		.build())
		.build();
	}
}