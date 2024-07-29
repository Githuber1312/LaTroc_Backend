package dev.artep.latroc_backend_simple.dto;

import dev.artep.latroc_backend_simple.model.LocalUser;
import dev.artep.latroc_backend_simple.model.Post;

import java.util.List;

public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private List<Post> posts;

    // Getters & Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // Constructors
    public UserResponse(LocalUser user, List<Post> posts) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.posts = posts;
    }
}
