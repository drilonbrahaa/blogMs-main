package com.example.blogMs.entities;

import java.util.Set;

import com.example.blogMs.enums.Category;
import com.example.blogMs.enums.Tag;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    
    private Set<Category> categories;
    
    private Set<Tag> tags;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> categories) {
        if (this.categories == null) {
            this.categories = categories;
        } else {
            for (Category category : categories) {
                if (category != null) {
                    this.categories.add(category);
                } 
            }
        } 
    }

    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) {
        if (this.tags == null) {
            this.tags = tags;
        } else {
            for (Tag tag : tags) {
                if (tag != null) {
                    this.tags.add(tag);
                }
            }   
        }
    }

}
