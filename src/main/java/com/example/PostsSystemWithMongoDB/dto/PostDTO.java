package com.example.PostsSystemWithMongoDB.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.PostsSystemWithMongoDB.entities.Post;

public class PostDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String author;
	
	@NotBlank(message = "{title.not.blank}")
	@Size(min = 4, max = 100, message = "{title.size}")
	private String title;
	
	@NotBlank(message = "{body.not.blank}")
	@Size(min = 4, message = "{body.size}")
	private String body;
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public Post dtoToPost(PostDTO postDto) {
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setBody(postDto.getBody());
		post.setAuthor(postDto.getAuthor());
		return post;
	}
}
