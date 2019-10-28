package com.songsite.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class AbstractEntity {
	@Id
	@GeneratedValue
	private Long id;
	
	//아이디 일치여부확인
		public boolean matchId(Long newId) {
			if(newId==null) {
				return false;
			}
			return newId.equals(id);
		}
		
	//생성 수정 날짜
	//@JsonProperty getter/setter 의 이름을 property 와 다른 이름을 사용할 수 있도록 설정한다. Database 를 자바 클래스로 매핑하는데 DB의 컬럼명이 알기 어려울 경우등에 유용하게 사용할 수 있다.
	@CreatedDate // 데이터가 생성된 시간
	private LocalDateTime createDate;

	@LastModifiedDate // 데이터가 수정된 시간
	private LocalDateTime modifiedDate;

	//시간
	public String getFormattedCreateDate() {
		if(createDate==null) {
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}
	
	public String getFormattedModifiedDate() {
		if(modifiedDate==null) {
			return "";
		}
		return modifiedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}

}