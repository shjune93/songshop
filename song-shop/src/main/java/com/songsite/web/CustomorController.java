package com.songsite.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.songsite.domain.Customer;

import com.songsite.message.Result;
import com.songsite.repository.CustomerRepository;

import com.songsite.session.HttpSessionUtils;

// (관리 페이지) 회원 관리 클래스
@Controller
@RequestMapping("customer")
public class CustomorController {

	@Autowired
	private CustomerRepository customerRepository;



	// #계정 삭제 FORM
	@GetMapping("/{{id}}")
	public String delete() {
		return "/customer/delete";
	}

	// #계정 정보 수정 FORM
	@GetMapping
	public String update(Model model, HttpSession session) {
		// 현제 로그인 중인 계정 ID (세션 값)
		String id = HttpSessionUtils.getUserFromSession(session).getUserId();
		model.addAttribute("customer", customerRepository.findById(id));
		return "/customer/update";
	}

	// #계정 정보 수정
	@PostMapping
	public String update(Customer newcustomer, Model model) {
		Customer customer = customerRepository.findByUserId(newcustomer.getUserId());

		customer.update(newcustomer);
		// 수정된 객체(계정) 업데이트
		customerRepository.save(customer);

		model.addAttribute("customer", customer);
		model.addAttribute(Result.MESSAGE_KEY, new Result("수정 완료"));
		return "/customer/update";
	}

	// #계정 생성 FORM
	@GetMapping("/create")
	public String createForm() {
		return "/customer/create";
	}
	
	@PostMapping("/create")
	public String create(Customer customer) {
		System.out.println("customer"+customer);
		//users.add(user);
		customerRepository.save(customer);
		return "redirect:/login";
	}


}