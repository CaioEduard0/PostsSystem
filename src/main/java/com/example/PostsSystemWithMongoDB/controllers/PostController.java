package com.example.PostsSystemWithMongoDB.controllers;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.PostsSystemWithMongoDB.dto.CommentDTO;
import com.example.PostsSystemWithMongoDB.dto.PostDTO;
import com.example.PostsSystemWithMongoDB.entities.Post;
import com.example.PostsSystemWithMongoDB.entities.User;
import com.example.PostsSystemWithMongoDB.services.PostService;
import com.example.PostsSystemWithMongoDB.services.UserService;

@Controller
public class PostController {
	
	private PostService postService;
	private UserService userService;
	
	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}
	
	@GetMapping("/")
	public ModelAndView indexPage(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("posts", postService.findAllPosts());
		mv.addObject("user", userService.findUserByEmail(userDetails.getUsername()));
		return mv;
	}
	
	@GetMapping("{userId}/posts")
	public ModelAndView userPostsPage(@PathVariable String userId) {
		ModelAndView mv = new ModelAndView("userpost");
		User user = userService.findUserById(userId);
		mv.addObject("posts", user.getPosts());
		return mv;
	}
	
	@GetMapping("post/{postId}")
	public ModelAndView postPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId) {
		ModelAndView mv = new ModelAndView("post");
		Post post = postService.findPostById(postId);
		mv.addObject("post", post);
		mv.addObject("comments", post.getComments());
		mv.addObject("user", userService.findUserByEmail(userDetails.getUsername()));
		return mv;
	}
	
	@GetMapping("new-post")
	public ModelAndView newPostPage(@AuthenticationPrincipal UserDetails userDetails) {
		ModelAndView mv = new ModelAndView("newpost");
		mv.addObject("user", userService.findUserByEmail(userDetails.getUsername()));
		return mv;
	}
	
	@PostMapping("new-post")
	public String createPost(@AuthenticationPrincipal UserDetails userDetails, @Valid PostDTO post, BindingResult bindingResult, Model model) {
		User user = userService.findUserByEmail(userDetails.getUsername());
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("user", user);
			return "newpost";
		}
		postService.insertPost(user, post.dtoToPost(post));
		return "redirect:/";
	}
	
	@DeleteMapping("post/delete/{postId}")
	public String deletePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId) {
		User user = userService.findUserByEmail(userDetails.getUsername());
		postService.deletePost(user, postId);
		return "redirect:/" + user.getId() + "/posts";
	}
	
	@PostMapping("post/{postId}/comment")
	public String newComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String postId, @Valid CommentDTO comment, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("errors", bindingResult.getAllErrors());
			model.addAttribute("user", userService.findUserByEmail(userDetails.getUsername()));
			Post post = postService.findPostById(postId);
			model.addAttribute("post", post);
			model.addAttribute("comments", post.getComments());
			return "post";
		}
		postService.insertComment(userService.findUserByEmail(userDetails.getUsername()), postId, comment);
		return "redirect:/post/" + postId;
	}
	
	@PostMapping("search")
	public String searchPost(@AuthenticationPrincipal UserDetails userDetails, String search, Model model) {
		model.addAttribute("user", userService.findUserByEmail(userDetails.getUsername()));
		model.addAttribute("posts", postService.findPostsBySearch(search));
		return "search";
	}
}
