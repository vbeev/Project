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
                     SummonRepository summonRepository, OfferRepository offerRepository,
                     PasswordEncoder passwordEncoder,
                     UserService userService) {
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
    adminUser.setCoins(100000);
    userRepository.save(adminUser);

    initSummon("OP1", 20, 20, adminUser);
    initSummon("OP2", 15, 10, adminUser);
    initSummon("WEAK", 3, 3, adminUser);
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

    initSummon("AER", 5, 15, normalUser);
    initSummon("GHN", 15, 10, normalUser);
    initSummon("YUH", 13, 12, normalUser);
    initSummon("LKS", 20, 10, normalUser);


    OfferEntity offerEntity = new OfferEntity(normalUser.getUsername(), OfferType.TRADE, summonEntity, normalUser);

    offerRepository.saveAndFlush(offerEntity);
  }

  private void initSummon(String name, int hp, int atk, UserEntity user) {

    SummonEntity summonEntity = new SummonEntity(name, hp, atk, user);
    summonRepository.saveAndFlush(summonEntity);
  }
}
