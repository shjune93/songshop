package com.songsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songsite.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	// 아이디 조회
	//Account findById(String id);
		
	// 아이디 조회
	User findByUserId(String id);
	
}
