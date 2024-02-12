package com.bank.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.bankapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Boolean existsByEmail(String email);
	Boolean existsByAccountNumber(String AccountNumber);
    User findByAccountNumber(String accountNumber);
}
