package com.songsite.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.songsite.domain.CategoryBoard;
import com.songsite.domain.CategoryBoardAnswer;
import com.songsite.domain.User;
import com.songsite.message.Result;
import com.songsite.repository.CategoryBoardAnswerRepository;
import com.songsite.repository.CategoryBoardRepository;
import com.songsite.session.HttpSessionUtils;

@RestController
@RequestMapping("/categoryboard/{categoryBoardId}/api/answers")
public class CategoryBoardAnswerController {
	@Autowired
	private CategoryBoardAnswerRepository categoryBoardAnswerRepository;
	
	@Autowired
	private CategoryBoardRepository categoryBoardRepository;
	
	@PostMapping("")
	public CategoryBoardAnswer create(@PathVariable Long categoryBoardId,String contents,HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return null;
		}
		User loginUser=HttpSessionUtils.getUserSession(session);
		
		
		CategoryBoard categoryBoard=categoryBoardRepository.findById(categoryBoardId).get();
		CategoryBoardAnswer answer=new CategoryBoardAnswer(loginUser,categoryBoard,contents);
		categoryBoard.addAnswer();//답변숫자 늘려줌
		return categoryBoardAnswerRepository.save(answer);
		//return String.format("redirect:/categoryBoards/%d", categoryBoardId);
	}
	
	@PostMapping("{id}/delete")
	public Result delete(@PathVariable Long categoryBoardId,@PathVariable Long id,HttpSession session) {
		if(!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인히야 합니다.");
		}
		CategoryBoardAnswer answer=categoryBoardAnswerRepository.findById(id).get();
		User loginUser=HttpSessionUtils.getUserSession(session);
		if(!answer.isSameWriter(loginUser)) {
			return Result.fail("자신의 글만 삭제할 수 있습니다.");
		}
		
		categoryBoardAnswerRepository.deleteById(id);
		CategoryBoard categoryBoard=categoryBoardRepository.findById(categoryBoardId).get();
		categoryBoard.deleteAnswer();//답변숫자 줄여줌
		categoryBoardRepository.save(categoryBoard);
		return Result.ok();
		
	}
}
