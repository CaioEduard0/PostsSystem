package com.example.PostsSystemWithMongoDB.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.PostsSystemWithMongoDB.entities.User;
import com.example.PostsSystemWithMongoDB.repositories.UserRepository;
import com.example.PostsSystemWithMongoDB.services.exceptions.RepeatedEmailException;

@Service
public class UserService implements UserDetailsService {
	
	private UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User findUserById(String id) {
		Optional<User> user = userRepository.findById(id);
		return user.get();
	}
	
	public User findUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElseThrow(() -> new UsernameNotFoundException("User with e-mail " + email + " not found!"));
	}
	
	public void insertUser(User user) {
		Optional<User> email = userRepository.findByEmail(user.getEmail());
		if (email.isPresent()) {
			throw new RepeatedEmailException(user.getEmail());
		}
		userRepository.insert(user);
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElseThrow(() -> new UsernameNotFoundException("User with e-mail " + email + " not found!"));
	}
}
