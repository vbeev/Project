package com.project.summoners_beta.model.entities;

import com.project.summoners_beta.model.enums.UserRoles;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles userRole;

    public UserRoleEntity() {
    }

    public UserRoleEntity(UserRoles userRole) {
        this.userRole = userRole;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public UserRoles getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoles userRole) {
        this.userRole = userRole;
    }
}
