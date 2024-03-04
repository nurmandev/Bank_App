package com.bank.bankapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.bankapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Boolean existsByEmail(String email);
	Optional<User> findByEmail(String email);
	Boolean existsByAccountNumber(String AccountNumber);
    User findByAccountNumber(String accountNumber);
}
