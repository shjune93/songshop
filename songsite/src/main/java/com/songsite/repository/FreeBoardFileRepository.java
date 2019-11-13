package com.songsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.songsite.domain.FreeBoard;
import com.songsite.domain.FreeBoardFile;

public interface FreeBoardFileRepository  extends JpaRepository<FreeBoardFile, Long>{

}
