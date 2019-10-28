package com.songsite.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;



import com.songsite.enums.Authority;
import com.songsite.enums.LoginType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
//@JsonProperty getter/setter 의 이름을 property 와 다른 이름을 사용할 수 있도록 설정한다. Database 를 자바 클래스로 매핑하는데 DB의 컬럼명이 알기 어려울 경우등에 유용하게 사용할 수 있다.
@Entity //개체인식
@Setter //setter생성
@Getter //getter생성
@ToString(exclude = "password") //비밀번호제외한 toString 자동생성
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성
public class Customer extends AbstractEntity {

	
	@Column(nullable=false, length=20 ,unique=true)
	private String userId;
	private String password;
	@Column(nullable=false, length=20 ,unique=true)
	private String name;
	@Column(nullable=false,unique=true)
	private String email = "";
	

	// 권한 (기본값 : BUYER(구매자))
	@Enumerated(EnumType.STRING)
	private Authority authority = Authority.BUYER;


	// 회원가입 타입 (기본값 : USERJSHOP(홈페이지 가입))
	@Enumerated(EnumType.STRING)
	private LoginType loginType = LoginType.USERJSHOP;

	// 아이디&비밀번호 확인 (로그인)
	public boolean machCustomer(Customer newCustomer) {
		if (newCustomer == null) {
			return false;
		}
		return this.userId.equals(newCustomer.userId) && this.password.equals(newCustomer.password);
	}
	//아이디 일치여부확인
	public boolean matchId(Long newId) {
		if(newId==null) {
			return false;
		}
		return newId.equals(getId());
	}

	// 계정 수정
	public void update(Customer newCustomer) {
		this.password=newCustomer.password;
	
	}

	// sns 가입 할경우 아이디,이름 저장
	public Customer(String userId, String name, LoginType loginType) {
		this.userId = userId;
		this.name = name;
		this.loginType = loginType;
	}

	// 회원 가입
	public void create(Customer newCustomer) {
		
		this.userId = newCustomer.userId;
		this.password = newCustomer.password;
		this.name = newCustomer.name;
		this.email=newCustomer.email;
	}

}
