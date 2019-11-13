package com.songsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songsite.domain.CategoryBoard;
import com.songsite.domain.FreeBoard;


public interface CategoryBoardRepository extends JpaRepository<CategoryBoard, Long>{

	
	
	List<CategoryBoard> findByTitleContaining(String title); //search query
	List<CategoryBoard> findByContentsContaining(String contents);//search query
	 
}
