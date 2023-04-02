package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
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
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new UserService(modelMapper, mockUserRepository,
                passwordEncoder, userDetailsService);

    }



    @Test
    void testUserFound() {

        UserEntity mockUserEntity = new UserEntity(EXISTING_USERNAME, PASSWORD, EXISTING_MAIL);

        when(mockUserRepository.findByUsername(EXISTING_USERNAME)).thenReturn(Optional.of(mockUserEntity));

        UserEntity testEntity = toTest.getUserByUsername(EXISTING_USERNAME);

        Assertions.assertNotNull(testEntity);
        Assertions.assertEquals(EXISTING_USERNAME, testEntity.getUsername());
        Assertions.assertEquals(PASSWORD, testEntity.getPassword());
        Assertions.assertEquals(EXISTING_MAIL, testEntity.getEmail());
    }

    @Test
    void testUserNotFound() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getUserByUsername(NOT_EXISTING_USERNAME);
                });
    }

}