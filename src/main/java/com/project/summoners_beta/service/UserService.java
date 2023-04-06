package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.exceptions.UserAlreadyExistsException;
import com.project.summoners_beta.model.dto.UserDTO;
import com.project.summoners_beta.model.dto.UserRegisterDTO;
import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.entities.PostEntity;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public UserService(ModelMapper modelMapper,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserDetailsService userDetailsService) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void registerUser(UserRegisterDTO userRegisterDTO,
                             Consumer<Authentication> successfulLoginProcessor) {

        if (emailExists(userRegisterDTO.getEmail()) || usernameExists(userRegisterDTO.getUsername())) {
            throw new UserAlreadyExistsException("Email or Username already exists!");
        }

        UserEntity user = new UserEntity();

        user.setUsername(userRegisterDTO.getUsername().trim());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword().trim()));
        user.setEmail(userRegisterDTO.getEmail().trim());

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userRegisterDTO.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userRegisterDTO.getPassword().trim(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
    }

    public boolean emailExists(String email) {
        return this.userRepository.findByEmail(email.trim()).isPresent();
    }

    public boolean usernameExists(String username) {
        return this.userRepository.findByUsername(username.trim()).isPresent();
    }

     public UserDTO getByUsername(String username) {
        return this.modelMapper
                .map(this.userRepository.findByUsername(username)
                        .orElseThrow(() -> new ObjectNotFoundException("No such user!")), UserDTO.class);
    }

    public Long getUserIdByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("No such user!")).getId();
    }

    public UserEntity getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("No such user!"));
    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("No such user!"));
    }

    public void updateUser(UserEntity userEntity) {
        this.userRepository.saveAndFlush(userEntity);
    }

    public void grantReward(String username) {
        UserEntity userEntity = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("No such user!"));

        userEntity.setCoins(userEntity.getCoins() + 5);

        this.userRepository.saveAndFlush(userEntity);
    }
}
