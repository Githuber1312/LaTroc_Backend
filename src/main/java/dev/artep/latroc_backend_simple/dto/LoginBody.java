package dev.artep.latroc_backend_simple.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginBody {
    @NotNull
    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    private String password;

    // Getters & Setters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String testUser) {
    }

    public void setPassword(String password) {
    }
}
