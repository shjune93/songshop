package com.songsite.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.songsite.repository.CustomerRepository;
import com.songsite.repository.FreeBoardRepository;
import com.songsite.session.HttpSessionUtils;

@Controller
@RequestMapping("freeboard")
public class FreeBoardContoller {
	@Autowired
	private FreeBoardRepository freeBoardRepository;

	// #로그인 FORM
	@GetMapping("")
	public String loginForm(HttpSession session, HttpServletRequest request) {
		if (session.getAttribute(HttpSessionUtils.CUSTOMER_SESSION_KEY) == null) {
			// 이미 로그인 상태일 경우
			return "redirect:/login";
		}
		return "/freeboard/form";
	}
}
