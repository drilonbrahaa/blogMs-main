package com.example.blogMs.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blogMs.entities.Comment;
import com.example.blogMs.entities.Post;
import com.example.blogMs.entities.User;
import com.example.blogMs.repositories.CommentRepository;
import com.example.blogMs.repositories.PostRepository;
import com.example.blogMs.repositories.UserRepository;

@Service
public class CommentService {
    
    @Autowired
    CommentRepository commentRepository;
    
    @Autowired
    PostRepository postRepository;
    
    @Autowired
    UserRepository userRepository;

    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment addComment(Long postId, String username, Comment comment) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findByUsername(username);
        if(!post.isPresent()) {
            throw new RuntimeException("Post not found");
        }
        if(!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        if(comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new RuntimeException("Error: Comment content cannot be empty!");
        }
        comment.setPost(post.get());
        comment.setUser(user.get());
        return commentRepository.save(comment);
    }

    public Comment editComment(Long commentId, Comment comment) {
        Comment editedComment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found"));
        if(comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new RuntimeException("Error: Comment content cannot be empty!");
        } else {
            editedComment.setContent(comment.getContent());
        }
        return commentRepository.save(editedComment);
    }
     
    public boolean deleteComment(Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return true;
        } else {
            return false;
        }
    }

}
