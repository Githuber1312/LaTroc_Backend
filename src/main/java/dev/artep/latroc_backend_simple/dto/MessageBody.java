package dev.artep.latroc_backend_simple.dto;

import jakarta.validation.constraints.NotBlank;

public class MessageBody {
    @NotBlank
    private String receiverUsername;

    @NotBlank
    private String content;

    // Getters & Setters
    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
