package com.example.blogMs.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogMs.entities.Post;
import com.example.blogMs.entities.User;
import com.example.blogMs.enums.Category;
import com.example.blogMs.enums.Tag;
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
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        if (post.getTitle() == null || post.getTitle().trim().isEmpty() || post.getContent() == null
                || post.getContent().trim().isEmpty()) {
            throw new RuntimeException("Error: Title and content cannot be empty!");
        }
        post.setAuthor(user.get());
        if (post.getCategories() == null) {
            post.setCategories(new HashSet<>());
        }
        if (post.getTags() == null) {
            post.setTags(new HashSet<>());
        }
        return postRepository.save(post);
    }

    public Post updatePost(Long id, Post post) {
        Post updatedPost = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getTitle() != null) {
            if (post.getTitle().trim().isEmpty()) {
                throw new RuntimeException("Error: Title cannot be empty!");
            }
            updatedPost.setTitle(post.getTitle());
        }

        if (post.getContent() != null) {
            if (post.getContent().trim().isEmpty()) {
                throw new RuntimeException("Error: Content cannot be empty!");
            }
            updatedPost.setContent(updatedPost.getContent());
        }

        if (post.getCategories() != null) {
            updatedPost.setCategories(post.getCategories());
        }

        if (post.getTags() != null) {
            updatedPost.setTags(post.getTags());
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

    public List<Post> filterByCategory(Category category) {
        List<Post> posts = postRepository.findAll();
        List<Post> postsByCategory = new ArrayList<>();
        for (Post post : posts) {
            if (post.getCategories().contains(category)) {
                postsByCategory.add(post);
            }
        }
        return postsByCategory;
    }

    public List<Post> filterByTag(Tag tag) {
        List<Post> posts = postRepository.findAll();
        List<Post> postsByTag = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTags().contains(tag)) {
                postsByTag.add(post);
            }
        }
        return postsByTag;
    }

}
