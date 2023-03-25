package com.project.summoners_beta.service;

import com.project.summoners_beta.model.dto.CreateSummonDTO;
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

    public void create(CreateSummonDTO createSummonDTO, String currentUserUsername) {
        UserDTO userDTO = this.userService.getByUsername(currentUserUsername);

      //  UserEntity userEntity = this.userService.getUserByUsername(currentUserUsername);

        SummonDTO summonDTO = new SummonDTO(createSummonDTO.getName(),
                createSummonDTO.getHp(), createSummonDTO.getAtk(), userDTO);

        SummonEntity summonEntity = this.modelMapper.map(summonDTO, SummonEntity.class);

        /*SummonEntity summonEntity = this.modelMapper.map(createSummonDTO, SummonEntity.class);

        summonEntity.setUser(userEntity);*/

        this.summonRepository.saveAndFlush(summonEntity);

    }

    public void updateSummon(SummonEntity summonEntity) {
        this.summonRepository.saveAndFlush(summonEntity);
    }

    public void deleteSummon(SummonEntity summonEntity) {
        this.summonRepository.delete(summonEntity);
    }
    public SummonEntity getSummonById(Long id) {
        return this.summonRepository.findById(id).get();
    }

    public SummonDTO getById(Long id) {
        return this.modelMapper.map(this.summonRepository.findById(id).orElseThrow(), SummonDTO.class);
    }

    public List<SummonDTO> getAllByUsername(String username) {

      //  this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return this.summonRepository.findAllByUser_Username(username)
                .orElseThrow()
                .stream()
                .map(summon -> this.modelMapper.map(summon, SummonDTO.class))
                .toList();
    }

    public long getCount() {
        return this.summonRepository.count();
    }
}
