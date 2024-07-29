package dev.artep.latroc_backend_simple.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @NotBlank
    @Size(max = 200)
    private String title;

    @NotNull @NotBlank
    @Size(max = 4000)
    private String description;

    @NotNull
    private double price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String imageUrl;

    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

    public LocalUser getUser() {
        return user;
    }

    public void setUser(LocalUser user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull @NotBlank @Size(max = 200) String getTitle() {
        return title;
    }

    public void setTitle(@NotNull @NotBlank @Size(max = 200) String title) {
        this.title = title;
    }

    public @NotNull @NotBlank @Size(max = 4000) String getDescription() {
        return description;
    }

    public void setDescription(@NotNull @NotBlank @Size(max = 4000) String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @NotNull
    public double getPrice() {
        return price;
    }

    public void setPrice(@NotNull double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
