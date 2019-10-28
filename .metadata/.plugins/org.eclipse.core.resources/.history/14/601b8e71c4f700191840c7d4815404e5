package com.userj.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.userj.domain.Account;
import com.userj.message.Result;
import com.userj.repository.AccountRepository;
import com.userj.session.HttpSessionUtils;

//  로그인 관리
@Controller
public class LoginController {

	@Autowired
	private AccountRepository accountRepository;

	// #로그인 FORM
	@GetMapping("/login")
	public String loginForm(HttpSession session, HttpServletRequest request) {
		if (session.getAttribute(HttpSessionUtils.ACCOUNT_SESSION_KEY) != null) {
			// 이미 로그인 상태일 경우
			return "redirect:/";
		}
		return "/login/login";
	}

	// 로그인
	@PostMapping("/login")
	public String login(Account loginAccount, HttpSession session, Model model,
			@RequestParam(required = false, defaultValue = "") String uri) {
		Account account = accountRepository.findById(loginAccount.getId());

		// 정보가 없는 회원 일경우..
		if (account == null) {
			model.addAttribute(Result.MESSAGE_KEY, new Result("존재 하지 않는 아이디입니다."));
			return "/login/login";
		}

		// 아이디 비밀번호 체크
		if (account.machAccount(loginAccount)) {
			// 로그인된 계정 세션에 저장
			HttpSessionUtils.save(session, account);

			return "redirect:/" + uri; // 로그인 완료
		}

		model.addAttribute(Result.MESSAGE_KEY, new Result("비밀번호가 틀렸습니다."));
		// 비밀번호가 틀릴경우
		return "/login/login";
	}

	// #로그아웃
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 모든 세션 삭제
		return "redirect:/";
	}
}
