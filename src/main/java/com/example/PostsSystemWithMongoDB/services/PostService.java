package com.example.PostsSystemWithMongoDB.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.PostsSystemWithMongoDB.dto.CommentDTO;
import com.example.PostsSystemWithMongoDB.entities.Post;
import com.example.PostsSystemWithMongoDB.entities.User;
import com.example.PostsSystemWithMongoDB.repositories.PostRepository;
import com.example.PostsSystemWithMongoDB.repositories.UserRepository;

@Service
public class PostService {

	private PostRepository postRepository;
	private UserRepository userRepository;
	
	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}
	
	public List<Post> findAllPosts() {
		return postRepository.findAll();
	}
	
	public Post findPostById(String id) {
		Optional<Post> post = postRepository.findById(id);
		return post.get();
	}
	
	public void insertPost(User user, Post post) {
		post.setAuthor(user.getName());
		postRepository.save(post);
		user.getPosts().add(post);
		userRepository.save(user);	
	}
	
	public void deletePost(User user, String id) {
		Post post = findPostById(id);
		user.getPosts().remove(post);
		userRepository.save(user);
		postRepository.deleteById(id);
	}
	
	public void insertComment(User user, String id, CommentDTO comment) {
		comment.setAuthor(user.getName());
		Post post = findPostById(id);
		post.getComments().add(comment);
		postRepository.save(post);
	}
	
	public List<Post> findPostsBySearch(String search) {
		return postRepository.searchPost(search);
	}
}
