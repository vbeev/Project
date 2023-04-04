package com.project.summoners_beta.service;

import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.entities.UserRoleEntity;
import com.project.summoners_beta.model.enums.UserRoles;
import com.project.summoners_beta.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRolesServiceTest {

    private final String USERNAME = "TestUsername";

    @Mock
    private UserService mockUserService;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    private UserRolesService toTest;

    @BeforeEach
    void setUp() {
        toTest = new UserRolesService(mockUserRoleRepository, mockUserService);
    }

    @Test
    void testSetAsMod() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        UserRoleEntity modRole = new UserRoleEntity(UserRoles.MODERATOR);

        when(mockUserRoleRepository.findUserRoleEntityByUserRole(UserRoles.MODERATOR))
                .thenReturn(Optional.of(modRole));

        toTest.setAsMod(mockUserEntity);

        Assertions.assertEquals(1, mockUserEntity.getRoles().size());
        Assertions.assertEquals(UserRoles.MODERATOR, mockUserEntity.getRoles().get(0).getUserRole());
    }

    @Test
    void testSetAsAdmin() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        UserRoleEntity adminRole = new UserRoleEntity(UserRoles.ADMIN);
        UserRoleEntity modRole = new UserRoleEntity(UserRoles.MODERATOR);

        List<UserRoleEntity> rolesList = new ArrayList<>();
        rolesList.add(modRole);
        rolesList.add(adminRole);

        when(mockUserRoleRepository.findAll()).thenReturn(rolesList);

        toTest.setAsAdmin(mockUserEntity);

        Assertions.assertEquals(2, mockUserEntity.getRoles().size());
        Assertions.assertEquals(UserRoles.MODERATOR, mockUserEntity.getRoles().get(0).getUserRole());
        Assertions.assertEquals(UserRoles.ADMIN, mockUserEntity.getRoles().get(1).getUserRole());
    }

    @Test
    void testSetAsNormalUser() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        UserRoleEntity modRole = new UserRoleEntity(UserRoles.MODERATOR);
        UserRoleEntity adminRole = new UserRoleEntity(UserRoles.ADMIN);

        mockUserEntity.getRoles().add(modRole);
        mockUserEntity.getRoles().add(adminRole);


        toTest.setAsNormalUser(mockUserEntity);

        Assertions.assertNull(mockUserEntity.getRoles());
    }
}
