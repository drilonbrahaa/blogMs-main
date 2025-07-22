package com.example.blogMs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogMs.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
