package com.example.blogMs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blogMs.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
}
