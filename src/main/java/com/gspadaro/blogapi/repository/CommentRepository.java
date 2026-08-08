package com.gspadaro.blogapi.repository;

import com.gspadaro.blogapi.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
