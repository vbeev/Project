package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.PostCreationDTO;
import com.project.summoners_beta.model.dto.PostDTO;
import com.project.summoners_beta.model.entities.PostEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    private final String USERNAME = "TestUsername";

    @Mock
    private PostRepository mockPostRepository;

    @Mock
    private UserService mockUserService;

    @Captor
    private ArgumentCaptor<PostEntity> postEntityArgumentCaptor;

    private PostService toTest;

    @BeforeEach
    void setUp() {
        toTest = new PostService(mockUserService, mockPostRepository, new ModelMapper());
    }

    @Test
    void testPostCreation() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        PostCreationDTO postCreationDTO = new PostCreationDTO("TestMessage");

        toTest.create(postCreationDTO, USERNAME);

        Mockito.verify(mockPostRepository).saveAndFlush(postEntityArgumentCaptor.capture());

        PostEntity savedPostEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertEquals(postCreationDTO.getContent(), savedPostEntity.getContent());
    }

    @Test
    void testPostRemoval() {

        PostEntity mockPostEntity = new PostEntity(USERNAME, "TestMessage", null);

        when(mockPostRepository.findById(1L)).thenReturn(Optional.of(mockPostEntity));

        toTest.removePost(1L);

        Mockito.verify(mockPostRepository).delete(postEntityArgumentCaptor.capture());

        PostEntity deletedPostEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertEquals(mockPostEntity.getContent(), deletedPostEntity.getContent());
    }

    @Test
    void testFailToRemovePost() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.removePost(2L);
                });
    }

    @Test
    void testGetPostDTOList() {

        PostEntity mockPostEntity1 = new PostEntity("FirstUser", "TestMessage1", null);
        PostEntity mockPostEntity2 = new PostEntity("SecondUser", "TestMessage2", null);

        List<PostEntity> mockEntityList = new ArrayList<>();

        mockEntityList.add(mockPostEntity1);
        mockEntityList.add(mockPostEntity2);

        when(mockPostRepository.findAll()).thenReturn(mockEntityList);

        List<PostDTO> postDTOList = toTest.getAll();

        Assertions.assertEquals(2, postDTOList.size());

        Assertions.assertEquals("TestMessage1", postDTOList.get(0).getContent());
        Assertions.assertEquals("FirstUser", postDTOList.get(0).getUsername());

        Assertions.assertEquals("TestMessage2", postDTOList.get(1).getContent());
        Assertions.assertEquals("SecondUser", postDTOList.get(1).getUsername());
    }
}
