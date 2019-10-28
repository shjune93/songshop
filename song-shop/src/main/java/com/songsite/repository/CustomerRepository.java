package com.songsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songsite.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
	
	// 아이디 조회
	//Account findById(String id);
		
	// 아이디 조회
	Customer findByUserId(String id);
	
}
