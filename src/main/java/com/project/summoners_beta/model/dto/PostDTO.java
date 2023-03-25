package com.project.summoners_beta.model.dto;

import jakarta.persistence.Column;

public class PostDTO {

    private Long id;
    private String username;
    private String content;

    public PostDTO() {
    }

    public PostDTO(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
