package com.songsite.domain;



import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;

import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Setter //setter생성
@Getter //getter생성
public class FreeBoardAnswer extends AbstractEntity{
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_answer_writer"))
	private User writer;
	
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_answer_to_freeboard"))
	private FreeBoard freeboard;
	
	@Lob
	private String contents;
	
	
	
	public FreeBoardAnswer() {
		
	}
	
	public FreeBoardAnswer(User writer,FreeBoard freeboard,String contents) {
		this.writer=writer;
		this.freeboard=freeboard;
		this.contents=contents;
		
	}
	
	

	

	@Override
	public String toString() {
		return "Answer [" + super.toString() + ", writer=" + writer + ", contents=" + contents + ",]";
	}
	
	public boolean isSameWriter(User loginUser) {
		return loginUser.equals(this.writer);
	}
	
	
}
