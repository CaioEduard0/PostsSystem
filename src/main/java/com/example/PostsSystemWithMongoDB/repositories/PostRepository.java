package com.example.PostsSystemWithMongoDB.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.PostsSystemWithMongoDB.entities.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
	
	@Query("{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } }, { 'comments.comment': { $regex: ?0, $options: 'i' } } ] }")
	List<Post> searchPost(String search);
}
