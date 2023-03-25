package com.project.summoners_beta.repository;

import com.project.summoners_beta.model.entities.UserRoleEntity;
import com.project.summoners_beta.model.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    Optional<UserRoleEntity> findUserRoleEntityByUserRole(UserRoles userRole);
}
