package com.songsite.session;

import javax.servlet.http.HttpSession;

import com.songsite.domain.Customer;
import com.songsite.enums.Authority;

// 홈페이지  세션 관리 클래스
public class HttpSessionUtils {

	// 사용자 세션
	public static final String CUSTOMER_SESSION_KEY = "sessionedCUSTOMER";

	// 관리자 세션
	public static final String MANAGER_SESSION_KEY = "sessionedManager";

	// 세션값에 sessionedCUSTOMER 값이 있다면 CUSTOMER 객체를 리턴 해준다.
	public static Customer getUserFromSession(HttpSession session) {
		if (session.getAttribute(CUSTOMER_SESSION_KEY) == null) {
			return null; // 세션 값이 없으면 null 리턴
		}
		return (Customer) session.getAttribute(CUSTOMER_SESSION_KEY);
	}

	public static void save(HttpSession session, Customer CUSTOMER) {
		// 로그인된 계정에 세션값 할당
		session.setAttribute(HttpSessionUtils.CUSTOMER_SESSION_KEY, CUSTOMER);

		// 권한 설정 MANAGER(관리) 세션값 할당
		if (CUSTOMER.getAuthority().equals(Authority.MANAGER)) {
			session.setAttribute(HttpSessionUtils.MANAGER_SESSION_KEY, "OK");
		}
	}

}
