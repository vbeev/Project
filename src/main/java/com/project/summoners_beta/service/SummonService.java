package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.CreateSummonDTO;
import com.project.summoners_beta.model.dto.SummonCreationDTO;
import com.project.summoners_beta.model.dto.SummonDTO;
import com.project.summoners_beta.model.dto.UserDTO;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.SummonRepository;
import com.project.summoners_beta.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SummonService {

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final SummonRepository summonRepository;

    public SummonService(ModelMapper modelMapper,
                         UserRepository userRepository,
                         UserService userService, SummonRepository summonRepository) {

        this.modelMapper = modelMapper;
        this.userService = userService;
        this.summonRepository = summonRepository;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public void create(SummonCreationDTO summonCreationDTO, String currentUserUsername) {

        UserEntity userEntity = this.userService.getUserByUsername(currentUserUsername);

        if ((userEntity.getCoins() - 25) < 0) {
            throw new NotEnoughCoinsException();
        }

        Random random = new Random();

        int hp = random.nextInt(60 - 30) + 30;
        int atk = random.nextInt(30 - 5) + 5;

        SummonEntity summonEntity = new SummonEntity(summonCreationDTO.getName(), hp, atk, userEntity);

        userEntity.setCoins(userEntity.getCoins() - 25);

        this.userService.updateUser(userEntity);

        this.summonRepository.saveAndFlush(summonEntity);
    }

    public void updateSummon(SummonEntity summonEntity) {
        this.summonRepository.saveAndFlush(summonEntity);
    }

    public void deleteSummon(SummonEntity summonEntity) {
        this.summonRepository.delete(summonEntity);
    }
    public SummonEntity getSummonById(Long id) {
        return this.summonRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Summon doesn't exist!"));
    }

    public SummonDTO getById(Long id) {
        return this.modelMapper.map(this.summonRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Summon doesn't exist!")), SummonDTO.class);
    }

    public List<SummonDTO> getAllByUsername(String username) {

        return this.summonRepository.findAllByUser_Username(username)
                .orElseThrow(() -> new ObjectNotFoundException("Could not get summons!"))
                .stream()
                .map(summon -> this.modelMapper.map(summon, SummonDTO.class))
                .toList();
    }

    public SummonDTO getRandomByNotUser(String username) {
       return this.modelMapper.map(this.summonRepository.findRandomNotByUser(username)
               .orElseThrow(() -> new ObjectNotFoundException("Summon doesn't exist!")), SummonDTO.class);
    }

    public long getCount() {
        return this.summonRepository.count();
    }
}
