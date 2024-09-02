package dev.artep.latroc_backend_simple.dto;

import dev.artep.latroc_backend_simple.model.Post;

public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private String username;
    private Long userId;
    private String imageUrl;
    private double price;
    private String category;

    public PostResponse() {

    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Constructors
    public PostResponse(Post post, String username, Long userId) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.username = username;
        this.userId = userId;
        this.imageUrl = post.getImageUrl();
        this.price = post.getPrice();
        this.category = post.getCategory().getName();
    }
}
