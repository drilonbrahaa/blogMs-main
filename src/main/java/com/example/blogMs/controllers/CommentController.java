package com.example.blogMs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogMs.entities.Comment;
import com.example.blogMs.entities.Post;
import com.example.blogMs.entities.User;
import com.example.blogMs.repositories.CommentRepository;
import com.example.blogMs.repositories.PostRepository;
import com.example.blogMs.repositories.UserRepository;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/post/{postId}")
    public List<Comment> getCommentsByPost(@PathVariable Long postId) {
        return commentRepository.findByPostId(postId);
    }
    
    @PostMapping("/post/{postId}")
    public Comment addComment(@PathVariable Long postId, @RequestParam String username, @RequestBody Comment comment) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findByUsername(username);
        if(!post.isPresent() || !user.isPresent()) {
            throw new RuntimeException("Post or User not found");
        }
        if(comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new RuntimeException("Error: Comment content cannot be empty!");
        }
        comment.setPost(post.get());
        comment.setUser(user.get());
        return commentRepository.save(comment);
    }
    
    @PutMapping("/{commentId}")
    public Comment editComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        Comment editedComment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if(comment.getContent() == null || comment.getContent().isEmpty()) {
            throw new RuntimeException("Error: Comment content cannot be empty!");
        } else {
            editedComment.setContent(comment.getContent());
        }
        return commentRepository.save(editedComment);
    }
    
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        commentRepository.deleteById(commentId);
        return "Comment deleted successfully";
    }
}
