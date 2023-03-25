package com.project.summoners_beta.service;


import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.entities.UserRoleEntity;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.model.enums.UserRoles;
import com.project.summoners_beta.repository.OfferRepository;
import com.project.summoners_beta.repository.SummonRepository;
import com.project.summoners_beta.repository.UserRepository;
import com.project.summoners_beta.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitService {

  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;

  private final SummonRepository summonRepository;
  private final OfferRepository offerRepository;
  private final PasswordEncoder passwordEncoder;

  private final UserService userService;

  public InitService(UserRoleRepository userRoleRepository,
                     UserRepository userRepository,
                     SummonRepository summonRepository, OfferRepository offerRepository, PasswordEncoder passwordEncoder,
                     @Value("${app.default.password}") String defaultPassword, UserService userService) {
    this.userRoleRepository = userRoleRepository;
    this.userRepository = userRepository;
    this.summonRepository = summonRepository;
    this.offerRepository = offerRepository;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  @PostConstruct
  public void init() {
    initRoles();
    initUsers();
  }

  private void initRoles() {
    if (userRoleRepository.count() == 0) {
      UserRoleEntity moderatorRole = new UserRoleEntity();
      moderatorRole.setUserRole(UserRoles.MODERATOR);

      UserRoleEntity adminRole = new UserRoleEntity();
      adminRole.setUserRole(UserRoles.ADMIN);

      userRoleRepository.save(moderatorRole);
      userRoleRepository.save(adminRole);
    }
  }

  private void initUsers() {
    if (userRepository.count() == 0) {
      initAdmin();
      initModerator();
      initNormalUser();
    }
  }

  private void initAdmin(){
    UserEntity adminUser =
            new UserEntity("AdminName",
                    passwordEncoder.encode("1234"),
                    "admin@mail.com");

    adminUser.setRoles(userRoleRepository.findAll());
    userRepository.save(adminUser);
  }

  private void initModerator(){

    UserRoleEntity moderatorRole = userRoleRepository.
        findUserRoleEntityByUserRole(UserRoles.MODERATOR).orElseThrow();

    UserEntity moderatorUser =
            new UserEntity("ModName",
                    passwordEncoder.encode("1234"),
                    "mod@mail.com");

    moderatorUser.setRoles(List.of(moderatorRole));
    userRepository.save(moderatorUser);
  }

  private void initNormalUser(){

    UserEntity normalUser =
            new UserEntity("NormalName",
                    passwordEncoder.encode("1234"),
                    "normal@mail.com");

    userRepository.saveAndFlush(normalUser);

    SummonEntity summonEntity = new SummonEntity("ABC", 20, 15, null);

    summonRepository.saveAndFlush(summonEntity);


    OfferEntity offerEntity = new OfferEntity(normalUser.getUsername(), OfferType.TRADE, summonEntity, normalUser);

    offerRepository.saveAndFlush(offerEntity);
  }
}
