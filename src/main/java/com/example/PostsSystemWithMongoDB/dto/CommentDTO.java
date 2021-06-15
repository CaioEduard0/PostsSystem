package com.example.PostsSystemWithMongoDB.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

public class CommentDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private LocalDateTime date = LocalDateTime.now();
	private String author;
	
	@NotBlank(message = "{comment.not.blank}")
	private String comment;
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
