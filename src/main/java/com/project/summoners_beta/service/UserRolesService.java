package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.UserRoleChangeDTO;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.entities.UserRoleEntity;
import com.project.summoners_beta.model.enums.UserRoles;
import com.project.summoners_beta.repository.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRolesService {

    private final UserRoleRepository userRoleRepository;

    private final UserService userService;

    public UserRolesService(UserRoleRepository userRoleRepository, UserService userService) {
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    public void changeRole(UserRoleChangeDTO userRoleChangeDTO) {

        UserEntity userEntity = this.userService.getUserByUsername(userRoleChangeDTO.getUsername());

        switch (userRoleChangeDTO.getRole()) {
            case "normal" -> setAsNormalUser(userEntity);
            case "mod" -> setAsMod(userEntity);
            case "admin" -> setAsAdmin(userEntity);
        }
    }

    public void setAsMod(UserEntity userEntity) {
        UserRoleEntity moderatorRole = this.userRoleRepository.
                findUserRoleEntityByUserRole(UserRoles.MODERATOR)
                .orElseThrow(() -> new ObjectNotFoundException("Could not get role!"));

        userEntity.setRoles(List.of(moderatorRole));

        this.userService.updateUser(userEntity);
    }

    public void setAsAdmin(UserEntity userEntity) {
        userEntity.setRoles(this.userRoleRepository.findAll());

        this.userService.updateUser(userEntity);
    }

    public void setAsNormalUser(UserEntity userEntity) {

        userEntity.setRoles(null);

        this.userService.updateUser(userEntity);
    }
}
