package com.songsite.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.songsite.domain.Customer;
import com.songsite.message.Result;
import com.songsite.repository.CustomerRepository;
import com.songsite.session.HttpSessionUtils;

//  로그인 관리
@Controller
public class LoginController {

	@Autowired
	private CustomerRepository CustomerRepository;

	// #로그인 FORM
	@GetMapping("login")
	public String loginForm(HttpSession session, HttpServletRequest request) {
		if (session.getAttribute(HttpSessionUtils.CUSTOMER_SESSION_KEY) != null) {
			// 이미 로그인 상태일 경우
			return "redirect:/";
		}
		return "/login/login";
	}

	// 로그인
	@PostMapping("login")
	public String login(Customer loginCustomer, HttpSession session, Model model) {
		Customer Customer = CustomerRepository.findByUserId(loginCustomer.getUserId());

		// 정보가 없는 회원 일경우..
		if (Customer == null) {
			model.addAttribute(Result.MESSAGE_KEY, new Result("존재 하지 않는 아이디입니다."));
			return "/login/login";
		}

		// 아이디 비밀번호 체크
		if (Customer.machCustomer(loginCustomer)) {
			// 로그인된 계정 세션에 저장
			HttpSessionUtils.save(session, Customer);

			return "redirect:/"; // 로그인 완료
		}

		model.addAttribute(Result.MESSAGE_KEY, new Result("비밀번호가 틀렸습니다."));
		// 비밀번호가 틀릴경우
		return "/login/login";
		
//		User user=userRepository.findByUserId(userId);
//		if (user == null) {
//			System.out.println("Login Failue!");
//			return "redirect:/users/loginForm";
//			
//		}
//		if(!user.matchPassword(password)) {
//
//			System.out.println("Login Failue!");
//			return "redirect:/users/loginForm";
//		}
//		
//		System.out.println("Login Success!");
//		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
//		
//		return "redirect:/";
	}

	// #로그아웃
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 모든 세션 삭제
		return "redirect:/";
	}
}
