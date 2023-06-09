package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.ConfirmOfferDTO;
import com.project.summoners_beta.model.dto.OfferDTO;
import com.project.summoners_beta.model.dto.TradeOfferDTO;
import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.repository.OfferRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserService userService;
    private final SummonService summonService;

    private final ModelMapper modelMapper;

    public OfferService(OfferRepository offerRepository, UserService userService,
                        SummonService summonService, ModelMapper modelMapper) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.summonService = summonService;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
    }

    public void createOffer(TradeOfferDTO tradeOfferDTO, OfferType offerType, String username) {
        UserEntity userEntity = this.userService.getUserByUsername(username);
        SummonEntity summonEntity = this.summonService.getSummonById(tradeOfferDTO.getSummonId());

        summonEntity.setUser(null);

        this.summonService.updateSummon(summonEntity);

        OfferEntity offerEntity = new OfferEntity(username, offerType, summonEntity, userEntity);

        if (offerType == OfferType.TRADE) {
            offerEntity.setPrice(0);
        }
        else {
            offerEntity.setPrice(tradeOfferDTO.getPrice());
        }

        this.offerRepository.saveAndFlush(offerEntity);
    }



    public void acceptOffer(TradeOfferDTO tradeOfferDTO,
                            ConfirmOfferDTO confirmOfferDTO, OfferType offerType, String username) {

        OfferEntity offerEntity = this.offerRepository.findById(confirmOfferDTO.getOfferId())
                .orElseThrow(() -> new ObjectNotFoundException("Offer doesn't exist!"));

        UserEntity currentUser = this.userService.getUserByUsername(username);
        UserEntity offerOwner = offerEntity.getUser();

        SummonEntity offerSummon = offerEntity.getSummonEntity();

        if (offerType == OfferType.TRADE) {

            SummonEntity selectedSummon = this.summonService.getSummonById(tradeOfferDTO.getSummonId());

            selectedSummon.setUser(offerOwner);

            this.summonService.updateSummon(selectedSummon);

            offerSummon.setUser(currentUser);
            this.summonService.updateSummon(offerSummon);

            this.offerRepository.delete(offerEntity);
        }
        else {

            if ((currentUser.getCoins() - offerEntity.getPrice()) < 0)
            {
                throw new NotEnoughCoinsException();
            }

            offerOwner.setCoins(offerOwner.getCoins() + offerEntity.getPrice());
            currentUser.setCoins(currentUser.getCoins() - offerEntity.getPrice());

            this.userService.updateUser(offerOwner);
            this.userService.updateUser(currentUser);

            offerSummon.setUser(currentUser);
            this.summonService.updateSummon(offerSummon);

            this.offerRepository.delete(offerEntity);
        }
    }

    public void removeOffer(ConfirmOfferDTO confirmOfferDTO, String username) {

        UserEntity currentUser = this.userService.getUserByUsername(username);

        OfferEntity offerEntity = offerRepository.findById(confirmOfferDTO.getOfferId())
                .orElseThrow(() -> new ObjectNotFoundException("Offer doesn't exist!"));

        SummonEntity summonEntity = offerEntity.getSummonEntity();

        summonEntity.setUser(currentUser);
        this.summonService.updateSummon(summonEntity);

        this.offerRepository.delete(offerEntity);
    }

    public List<OfferDTO> getAllOffersByUsername(String username) {
        return this.offerRepository.findAllByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("Could not get offers!"))
                .stream()
                .map(offer -> this.modelMapper.map(offer, OfferDTO.class))
                .toList();
    }

    public List<OfferDTO> getAll() {

        return this.offerRepository.findAll()
                .stream()
                .map(offer -> this.modelMapper.map(offer, OfferDTO.class))
                .toList();
    }

    public List<OfferDTO> getAllByTypeAndByNotUser(OfferType offerType, String username) {

        return this.offerRepository.findAllByOfferTypeAndUsernameNot(offerType, username)
                .orElseThrow(() -> new ObjectNotFoundException("Could not get offers!"))
                .stream()
                .map(offer -> this.modelMapper.map(offer, OfferDTO.class))
                .toList();
    }

}
