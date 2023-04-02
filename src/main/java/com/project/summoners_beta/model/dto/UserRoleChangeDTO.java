package com.project.summoners_beta.model.dto;

public class UserRoleChangeDTO {

    private String username;

    private String role;

    public UserRoleChangeDTO() {
    }

    public UserRoleChangeDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
