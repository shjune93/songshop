package com.songsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songsite.domain.FreeBoard;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>{//스프링프레임웍이 구현체 생성,인스턴스 까지 만들어서 관리
	
}
