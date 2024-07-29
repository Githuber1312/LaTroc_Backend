package dev.artep.latroc_backend_simple.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostBody {
    @NotBlank @NotNull
    @Size(max = 200)
    String title;

    @NotBlank @NotNull
    @Size(max = 2000)
    String description;

    @NotNull
    double price;

    @NotBlank @NotNull
    Long category;


    // Getters & Setters
    public @NotBlank @NotNull @Size(max = 200) String getTitle() {
        return title;
    }

    public @NotBlank @NotNull @Size(max = 2000) String getDescription() {
        return description;
    }

    @NotNull
    public double getPrice() {
        return price;
    }

    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(@NotBlank @NotNull @Size(max = 200) String title) {
        this.title = title;
    }

    public void setDescription(@NotBlank @NotNull @Size(max = 2000) String description) {
        this.description = description;
    }

    public void setPrice(@NotNull double price) {
        this.price = price;
    }

    public @NotBlank @NotNull Long getCategoryId() {
        return category;
    }

    public void setCategoryId(@NotBlank @NotNull Long category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
