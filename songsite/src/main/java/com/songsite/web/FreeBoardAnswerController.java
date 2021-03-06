package com.songsite.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.songsite.domain.FreeBoard;
import com.songsite.domain.FreeBoardAnswer;
import com.songsite.domain.User;
import com.songsite.message.Result;

import com.songsite.repository.FreeBoardAnswerRepository;
import com.songsite.repository.FreeBoardRepository;

import com.songsite.session.HttpSessionUtils;



@RestController
@RequestMapping("/freeboard/{freeBoardId}/api/answers")
public class FreeBoardAnswerController {
	@Autowired
	private FreeBoardAnswerRepository freeBoardAnswerRepository;
	
	@Autowired
	private FreeBoardRepository freeBoardRepository;
	
	@PostMapping("")
	public FreeBoardAnswer create(@PathVariable Long freeBoardId,String contents,HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}
		User loginUser=HttpSessionUtils.getUserSession(session);
		
		
		FreeBoard freeBoard=freeBoardRepository.findById(freeBoardId).get();
		FreeBoardAnswer answer=new FreeBoardAnswer(loginUser,freeBoard,contents);
		freeBoard.addAnswer();//답변숫자 늘려줌
		return freeBoardAnswerRepository.save(answer);
		//return String.format("redirect:/freeBoards/%d", freeBoardId);
	}
	
	@PostMapping("{id}/delete")
	public Result delete(
			@PathVariable Long freeBoardId,
			@PathVariable Long id,
			HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인하셔야 합니다.");
		}
		FreeBoardAnswer answer=freeBoardAnswerRepository.findById(id).get();
		User loginUser=HttpSessionUtils.getUserSession(session);
		if(!answer.isSameWriter(loginUser)) {
			return Result.fail("자신의 글만 삭제할 수 있습니다.");
		}
		
		freeBoardAnswerRepository.deleteById(id);
		FreeBoard freeBoard=freeBoardRepository.findById(freeBoardId).get();
		freeBoard.deleteAnswer();//답변숫자 줄여줌
		freeBoardRepository.save(freeBoard);
		return Result.ok();
		
	}
}
