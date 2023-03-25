package com.project.summoners_beta.service;

import com.project.summoners_beta.model.dto.PostCreationDTO;
import com.project.summoners_beta.model.dto.PostDTO;
import com.project.summoners_beta.model.entities.PostEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostService(UserService userService, PostRepository postRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public void create(PostCreationDTO postCreationDTO, String currentUserUsername) {

        UserEntity userEntity = this.userService.getUserByUsername(currentUserUsername);


        PostEntity postEntity = new PostEntity(
                currentUserUsername,
                postCreationDTO.getContent(),
                userEntity);

       /* postEntity.setUsername(currentUserUsername);
        postEntity.setContent(postCreationDTO.getContent());
        postEntity.setUser(userEntity);*/

        this.postRepository.saveAndFlush(postEntity);
    }

    public List<PostEntity> getAllPosts() {
        return this.postRepository.findAll();
    }

    public List<PostDTO> getAll() {
        return this.postRepository.findAll()
                .stream()
                .map(post -> this.modelMapper.map(post, PostDTO.class))
                .toList();
    }
}
