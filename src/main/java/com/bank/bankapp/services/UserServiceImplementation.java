package com.bank.bankapp.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.bankapp.config.JwtTokenProvider;
import com.bank.bankapp.dto.AccountInfo;
import com.bank.bankapp.dto.BankResponse;
import com.bank.bankapp.dto.CreditDebitRequest;
import com.bank.bankapp.dto.EmailDetails;
import com.bank.bankapp.dto.EnquiryRequest;
import com.bank.bankapp.dto.GetAllUsers;
import com.bank.bankapp.dto.LoginDto;
import com.bank.bankapp.dto.TransactionDto;
import com.bank.bankapp.dto.TransferRequest;
import com.bank.bankapp.dto.UserRequest;
import com.bank.bankapp.entity.Role;
import com.bank.bankapp.entity.User;
import com.bank.bankapp.repository.UserRepository; 
import com.bank.bankapp.utils.AccountUtils;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserServiceImplementation  implements UserService  {

	@Autowired
	UserRepository userRepository;


	@Autowired
	TransactionServices transactionServices;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	EmailService emailService;

    
	// REISTER AN ACCOUNT

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
				 .password(passwordEncoder.encode(userRequest.getPassword()))
				 .phoneNumber(userRequest.getPhoneNumber())
				 .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				 .status("ACTIVE")
				 .role(Role.valueOf("ROLE_USER"))
				 .build();


		 // Save the new user to the database
	        User savedUser = userRepository.save(newUser);

//			Email Service Alert


		 EmailDetails emailDetails = EmailDetails.builder()
				 .recipient(savedUser.getEmail())
				 .subject("ACCOUNT CREATION")
				 .messageBody("Dear " + savedUser.getFirstName() + " " + savedUser.getLastName()
				  +" Congratulation! your Account (number: "+
				   savedUser.getAccountNumber()+") with password( "  +" ) has been Successfully Created."
				 )
				 .build();

		 emailService.sendEmailAlert(emailDetails);

//		 Return a response

		 return BankResponse.builder()
				 .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				 .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				 .accountInfo(AccountInfo.builder()
						 .accountBalance(savedUser.getAccountBalance())
						 .accountNumber(savedUser.getAccountNumber())
						 .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
						 .build())
				 .build();
	 }

	//  LOGIN 
	public BankResponse login(LoginDto loginDto){
		Authentication authentication = null;
		authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
		);

		System.out.println(jwtTokenProvider.generateToken(authentication));
		
		EmailDetails loginAlert = EmailDetails.builder()
		.subject("You're logged in")
		.recipient(loginDto.getEmail())
		.messageBody("You logged into your account. if you did not initiate this request, please contact your bank")
		.build();

		emailService.sendEmailAlert(loginAlert);


		return BankResponse.builder()
		.responseCode("Login success")
		.responseMessage(jwtTokenProvider.generateToken(authentication))
		.build();
	}


	// BALANCE ENQUIRY
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


	// NAME ENQUIRY



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



	// CREDIT ACCOUNT


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

			// Send Email
		EmailDetails creditAlert = EmailDetails.builder()
			.subject("CREDIT ALERT")
			.recipient(userToCredit.getEmail())
			.messageBody("The sum of "+ request.getAmount() + " has been deposit to your account from "
			+ userToCredit.getFirstName())
			.build();

		emailService.sendEmailAlert(creditAlert);

		//  Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
		.accountNumber(userToCredit.getAccountNumber())
		.transactionType("CREDIT")
		.amount(request.getAmount())
		.build();


		transactionServices.saveTransaction(transactionDto);

	


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

	// DEBIT ACCOUNT

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponse.builder()
			.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			.accountInfo(null).build();
		}

		User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
		BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
		BigInteger debitAmount = request.getAmount().toBigInteger();

		if (availableBalance.intValue() < debitAmount.intValue()) {
			return BankResponse.builder()
			.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
			.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
			.accountInfo(null)
			.build();

		}else{
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepository.save(userToDebit);
		}

			// Send Email
			EmailDetails debitAlert = EmailDetails.builder()
			.subject("DEBIT ALERT")
			.recipient(userToDebit.getEmail())
			.messageBody("The sum of "+ request.getAmount() + " has been debited from your account to "
			+ request.getAccountNumber())
			.build();

		emailService.sendEmailAlert(debitAlert);

		//  Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
		.accountNumber(userToDebit.getAccountNumber())
		.transactionType("CREDIT")
		.amount(request.getAmount())
		.build();

		transactionServices.saveTransaction(transactionDto);

		return BankResponse.builder()
			.responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
			.responseMessage(AccountUtils.ACCOUNT_DEBIT_MESSAGE)
			.accountInfo(AccountInfo.builder()
			.accountNumber(request.getAccountNumber())
			.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
			.accountBalance(userToDebit.getAccountBalance())
			.build())
			.build();
	}

	// TRANSFER

	@Override
	public BankResponse transfer(TransferRequest request) {
		// get the account to debit (check if it's exists)
		// check if the amount I'm debiting is not more than the current balance
		// debit the account
		// get the account to debit
		// cretid the account
		boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		if (!isDestinationAccountExist) {
			return BankResponse.builder()
			.responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
			.responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
			.accountInfo(null).build();			
		}

		User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
					.accountInfo(null)
		.build();
		}
		sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
		String sourceUserName = sourceAccountUser.getAccountNumber() + " " + sourceAccountUser.getLastName();
		userRepository.save(sourceAccountUser);

		// Send Email
		EmailDetails debitAlert = EmailDetails.builder()
			.subject("DEBIT ALERT")
			.recipient(sourceAccountUser.getEmail())
			.messageBody("The sum of "+ request.getAmount() + " has been deducted from your current balance "
			 + sourceAccountUser.getAccountBalance() + " To" + sourceUserName)
			.build();

		emailService.sendEmailAlert(debitAlert);

		System.out.println("The sum of "+ request.getAmount() +  "has been deducted from your current balance "
 + sourceAccountUser.getAccountBalance());
		User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));

		userRepository.save(destinationAccountUser);

		// Send Email
		EmailDetails creditAlert = EmailDetails.builder()
			.subject("CREDIT ALERT")
			.recipient(sourceAccountUser.getEmail())
			.messageBody("The sum of "+ request.getAmount() + "has been sent to your account from "
			 + sourceUserName)
			.build();

		emailService.sendEmailAlert(creditAlert);

		System.out.println("The sum of "+ " " +
		 request.getAmount() + "has been sent to your account from " 
		  + sourceUserName);

		//  Save Transaction
		TransactionDto transactionDto = TransactionDto.builder()
		.accountNumber(destinationAccountUser.getAccountNumber())
		.transactionType("CREDIT")
		.amount(request.getAmount())
		.build();

		transactionServices.saveTransaction(transactionDto);
		
		return BankResponse.builder()
		.responseCode(AccountUtils.TRANSFER_SUCCESSFUl_CODE)
			.responseMessage(AccountUtils.TRANSFER_SUCCESSFULLY_MESSAGE)
			.accountInfo(null).build();		
	}

	
	// GET ALL USERS
	@Override
	public BankResponse getAllUsers() {
        return (BankResponse) userRepository.findAll();
    }
}
