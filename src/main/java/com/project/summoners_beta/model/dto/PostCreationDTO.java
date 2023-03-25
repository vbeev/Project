package com.project.summoners_beta.model.dto;

public class PostCreationDTO {

    private String content;

    public PostCreationDTO() {
    }

    public PostCreationDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
