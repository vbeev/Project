package com.project.summoners_beta.service;

import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.entities.UserRoleEntity;
import com.project.summoners_beta.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
    }

    private UserDetails map(UserEntity userEntity) {
        return new User(userEntity.getUsername(), userEntity.getPassword(), extractAuthority(userEntity));
    }

    private List<GrantedAuthority> extractAuthority(UserEntity userEntity) {
        return userEntity
                .getRoles()
                .stream().
                map(this::mapRole).
                toList();
    }

    private GrantedAuthority mapRole(UserRoleEntity userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getUserRole().name());
    }
}
