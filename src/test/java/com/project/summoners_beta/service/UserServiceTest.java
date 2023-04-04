package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.UserDTO;
import com.project.summoners_beta.model.dto.UserRegisterDTO;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final String EXISTING_USERNAME = "UserExist";
    private final String NOT_EXISTING_USERNAME = "UserNotExist";
    private final String EXISTING_MAIL = "exist@mail.com";

    private final String PASSWORD = "pass";

    private final String NOT_EXISTING_MAIL = "not_exist@mail.com";
    private UserService toTest;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new UserService(new ModelMapper(), mockUserRepository,
                passwordEncoder, userDetailsService);

    }



    @Test
    void testUserEntityFound() {

        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(mockUserEntity));

        UserEntity testEntity = toTest.getUserByUsername(EXISTING_USERNAME);

        Assertions.assertNotNull(testEntity);
        Assertions.assertEquals(EXISTING_USERNAME, testEntity.getUsername());
        Assertions.assertEquals(PASSWORD, testEntity.getPassword());
        Assertions.assertEquals(EXISTING_MAIL, testEntity.getEmail());
    }

    @Test
    void testUserEntityNotFound() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getUserByUsername(NOT_EXISTING_USERNAME);
                });
    }

    @Test
    void testUserDTOFound() {

        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(mockUserEntity));

        UserDTO testUserDTO = toTest.getByUsername(EXISTING_USERNAME);

        Assertions.assertNotNull(testUserDTO);
        Assertions.assertEquals(EXISTING_USERNAME, testUserDTO.getUsername());
        Assertions.assertEquals(EXISTING_MAIL, testUserDTO.getEmail());
    }

    @Test
    void testGrantReward() {
        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(mockUserEntity));

        toTest.grantReward(EXISTING_USERNAME);

        Assertions.assertEquals(105, mockUserEntity.getCoins());
    }

    @Test
    void testFailToGrantRewardToAUser() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.grantReward(NOT_EXISTING_USERNAME);
                });
    }

    @Test
    void testEmailExists() {
        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByEmail(EXISTING_MAIL)).thenReturn(Optional.of(mockUserEntity));

        boolean result = toTest.emailExists(EXISTING_MAIL);

        Assertions.assertTrue(result);
    }

    @Test
    void testUsernameExists() {
        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(mockUserEntity));

        boolean result = toTest.usernameExists(EXISTING_USERNAME);

        Assertions.assertTrue(result);
    }
}