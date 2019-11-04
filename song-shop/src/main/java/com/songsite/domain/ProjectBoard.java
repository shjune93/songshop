package com.songsite.domain;


import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



public class ProjectBoard extends AbstractEntity{
	@ManyToOne
	@JoinColumn(foreignKey=@ForeignKey(name="fk_freeboard_writer"))
	private User writer;
	
	private String title;
	
	private String contents;
	
	private String link;
	
	
	public ProjectBoard() {}
	
	
	public ProjectBoard(User writer, String title,String link, String contents) {
		
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.setFormattedCreateDate();
	}
	
	
	
	public void update(String title,String link,String contents) {
		this.title=title;
		this.contents=contents;
		this.link=link;
		this.setFormattedModifiedDate();
		
	}

	public boolean isSameWriter(User loginUser) {
		//System.out.println(this.writer.getId());
		//System.out.println(loginUser.getId());
		return this.writer.matchId(loginUser.getId());
	}

	
}
