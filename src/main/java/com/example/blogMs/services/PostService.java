package com.example.blogMs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogMs.entities.Post;
import com.example.blogMs.entities.User;
import com.example.blogMs.repositories.PostRepository;
import com.example.blogMs.repositories.UserRepository;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(Post post, String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        if (post.getTitle() == null || post.getTitle().isEmpty() || post.getContent() == null || post.getContent().isEmpty()) {
            throw new RuntimeException("Error: Title and content cannot be empty!");
        }
        post.setAuthor(user.get());
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post post) {
        Post updatedPost = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (post.getTitle().isEmpty()) {
            throw new RuntimeException("Error: Title cannot be empty!");
        } else if (post.getTitle() != null) {
            updatedPost.setTitle(post.getTitle());
        }

        if (post.getContent().isEmpty()) {
            throw new RuntimeException("Error: Content cannot be empty!");
        } else if (post.getContent() != null) {
            updatedPost.setContent(updatedPost.getContent());
        }

        return postRepository.save(updatedPost);
    }
    
    public Boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }
        
}
