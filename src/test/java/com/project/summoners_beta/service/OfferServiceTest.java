package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.ConfirmOfferDTO;
import com.project.summoners_beta.model.dto.OfferDTO;
import com.project.summoners_beta.model.dto.SummonDTO;
import com.project.summoners_beta.model.dto.TradeOfferDTO;
import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.repository.OfferRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OfferServiceTest {

    private final String USERNAME = "TestUsername";

    @Mock
    private UserService mockUserService;

    @Mock
    private SummonService mockSummonService;

    @Mock
    private OfferRepository mockOfferRepository;

    @Captor
    private ArgumentCaptor<OfferEntity> offerEntityArgumentCaptor;

    private OfferService toTest;

    @BeforeEach
    void setUp() {
        toTest = new OfferService(mockOfferRepository, mockUserService, mockSummonService, new ModelMapper());
    }

    @Test
    void testTradeOfferCreation() {
        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity = new SummonEntity("TestSummon", 10, 15, mockUserEntity);

        TradeOfferDTO tradeOfferDTO = new TradeOfferDTO(1L, null);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        when(mockSummonService.getSummonById(tradeOfferDTO.getSummonId())).thenReturn(mockSummonEntity);

        toTest.createOffer(tradeOfferDTO, OfferType.TRADE, USERNAME);

        Mockito.verify(mockOfferRepository).saveAndFlush(offerEntityArgumentCaptor.capture());

        OfferEntity savedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(OfferType.TRADE, savedOfferEntity.getOfferType());
        Assertions.assertEquals(mockSummonEntity, savedOfferEntity.getSummonEntity());
        Assertions.assertNull(savedOfferEntity.getSummonEntity().getUser());
        Assertions.assertEquals(mockUserEntity, savedOfferEntity.getUser());
        Assertions.assertEquals(mockUserEntity.getUsername(), savedOfferEntity.getUser().getUsername());
        Assertions.assertEquals(0, savedOfferEntity.getPrice());
    }

    @Test
    void testSellOfferCreation() {
        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity = new SummonEntity("TestSummon", 10, 15, mockUserEntity);

        TradeOfferDTO tradeOfferDTO = new TradeOfferDTO(1L, 20);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        when(mockSummonService.getSummonById(tradeOfferDTO.getSummonId())).thenReturn(mockSummonEntity);

        toTest.createOffer(tradeOfferDTO, OfferType.SELL, USERNAME);

        Mockito.verify(mockOfferRepository).saveAndFlush(offerEntityArgumentCaptor.capture());

        OfferEntity savedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(OfferType.SELL, savedOfferEntity.getOfferType());
        Assertions.assertEquals(mockSummonEntity, savedOfferEntity.getSummonEntity());
        Assertions.assertNull(savedOfferEntity.getSummonEntity().getUser());
        Assertions.assertEquals(mockUserEntity, savedOfferEntity.getUser());
        Assertions.assertEquals(mockUserEntity.getUsername(), savedOfferEntity.getUser().getUsername());
        Assertions.assertEquals(20, savedOfferEntity.getPrice());
    }

    @Test
    void testAcceptTradeOffer() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity = new SummonEntity("TestSummon", 10, 15, mockUserEntity);

        TradeOfferDTO tradeOfferDTO = new TradeOfferDTO(1L, null);

        ConfirmOfferDTO confirmOfferDTO = new ConfirmOfferDTO(1L);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        when(mockSummonService.getSummonById(tradeOfferDTO.getSummonId())).thenReturn(mockSummonEntity);

        SummonEntity mockOfferSummonEntity = new SummonEntity("OfferTestSummon", 20, 10, null);

        UserEntity mockOfferUserEntity =
                new UserEntity("OfferUsername", "testPass", "test@mail.com");

        OfferEntity mockOfferEntity = new OfferEntity(mockOfferUserEntity.getUsername(),
                OfferType.TRADE, mockOfferSummonEntity, mockOfferUserEntity);

        when(mockOfferRepository.findById(confirmOfferDTO.getOfferId())).thenReturn(Optional.of(mockOfferEntity));

        toTest.acceptOffer(tradeOfferDTO, confirmOfferDTO, mockOfferEntity.getOfferType(), USERNAME);

        Mockito.verify(mockOfferRepository).delete(offerEntityArgumentCaptor.capture());

        OfferEntity deletedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(mockUserEntity.getUsername(),
                mockOfferSummonEntity.getUser().getUsername());

        Assertions.assertEquals(mockOfferEntity.getUser().getUsername(),
                mockSummonEntity.getUser().getUsername());

        Assertions.assertEquals(mockOfferEntity.getUsername(), deletedOfferEntity.getUsername());

    }

    @Test
    void testAcceptSellOffer() {
        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        TradeOfferDTO tradeOfferDTO = new TradeOfferDTO(1L, null);

        ConfirmOfferDTO confirmOfferDTO = new ConfirmOfferDTO(1L);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        SummonEntity mockOfferSummonEntity = new SummonEntity("OfferTestSummon", 20, 10, null);

        UserEntity mockOfferUserEntity =
                new UserEntity("OfferUsername", "testPass", "test@mail.com");

        OfferEntity mockOfferEntity = new OfferEntity(mockOfferUserEntity.getUsername(),
                OfferType.SELL, mockOfferSummonEntity, mockOfferUserEntity);

        mockOfferEntity.setPrice(30);

        when(mockOfferRepository.findById(confirmOfferDTO.getOfferId())).thenReturn(Optional.of(mockOfferEntity));

        toTest.acceptOffer(tradeOfferDTO, confirmOfferDTO, mockOfferEntity.getOfferType(), USERNAME);

        Mockito.verify(mockOfferRepository).delete(offerEntityArgumentCaptor.capture());

        OfferEntity deletedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(mockUserEntity.getUsername(),
                mockOfferSummonEntity.getUser().getUsername());

        Assertions.assertEquals(70, mockUserEntity.getCoins());

        Assertions.assertEquals(130, mockOfferUserEntity.getCoins());

        Assertions.assertEquals(mockOfferEntity.getUsername(), deletedOfferEntity.getUsername());
    }

    @Test
    void testAcceptSellOfferShouldFailIfNotEnoughCoins() {
        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        mockUserEntity.setCoins(0);

        TradeOfferDTO tradeOfferDTO = new TradeOfferDTO(1L, null);

        ConfirmOfferDTO confirmOfferDTO = new ConfirmOfferDTO(1L);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        SummonEntity mockOfferSummonEntity = new SummonEntity("OfferTestSummon", 20, 10, null);

        UserEntity mockOfferUserEntity =
                new UserEntity("OfferUsername", "testPass", "test@mail.com");

        OfferEntity mockOfferEntity = new OfferEntity(mockOfferUserEntity.getUsername(),
                OfferType.SELL, mockOfferSummonEntity, mockOfferUserEntity);

        mockOfferEntity.setPrice(30);

        when(mockOfferRepository.findById(confirmOfferDTO.getOfferId())).thenReturn(Optional.of(mockOfferEntity));

        assertThrows(NotEnoughCoinsException.class,
                () -> {
                    toTest.acceptOffer(tradeOfferDTO, confirmOfferDTO, mockOfferEntity.getOfferType(), USERNAME);
                });
    }

    @Test
    void testRemoveOffer() {
        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        ConfirmOfferDTO confirmOfferDTO = new ConfirmOfferDTO(1L);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        SummonEntity mockOfferSummonEntity = new SummonEntity("OfferTestSummon", 20, 10, null);

        UserEntity mockOfferUserEntity =
                new UserEntity("OfferUsername", "testPass", "test@mail.com");

        OfferEntity mockOfferEntity = new OfferEntity(mockOfferUserEntity.getUsername(),
                null, mockOfferSummonEntity, mockOfferUserEntity);

        when(mockOfferRepository.findById(confirmOfferDTO.getOfferId())).thenReturn(Optional.of(mockOfferEntity));

        toTest.removeOffer(confirmOfferDTO, USERNAME);

        Mockito.verify(mockOfferRepository).delete(offerEntityArgumentCaptor.capture());

        OfferEntity deletedOfferEntity = offerEntityArgumentCaptor.getValue();

        Assertions.assertEquals(USERNAME, mockOfferSummonEntity.getUser().getUsername());

        Assertions.assertEquals(mockOfferEntity.getUsername(), deletedOfferEntity.getUsername());
    }

    @Test
    void testGetOfferDTOListByUsername() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity1 = new SummonEntity("TestSummon1", 10, 15, mockUserEntity);
        SummonEntity mockSummonEntity2 = new SummonEntity("TestSummon2", 10, 15, mockUserEntity);

        OfferEntity mockOfferEntity1 = new OfferEntity(USERNAME, OfferType.TRADE, mockSummonEntity1, mockUserEntity);
        mockOfferEntity1.setPrice(0);

        OfferEntity mockOfferEntity2 = new OfferEntity(USERNAME, OfferType.SELL, mockSummonEntity2, mockUserEntity);
        mockOfferEntity2.setPrice(20);

        List<OfferEntity> mockEntityList = new ArrayList<>();

        mockEntityList.add(mockOfferEntity1);
        mockEntityList.add(mockOfferEntity2);

        when(mockOfferRepository.findAllByUsername(USERNAME)).thenReturn(Optional.of(mockEntityList));

        List<OfferDTO> offerDTOList = toTest.getAllOffersByUsername(USERNAME);

        Assertions.assertEquals(2, offerDTOList.size());

        Assertions.assertEquals(USERNAME, offerDTOList.get(0).getUserDTO().getUsername());
        Assertions.assertEquals(mockSummonEntity1.getName(), offerDTOList.get(0).getSummonDTO().getName());
        Assertions.assertEquals(0, offerDTOList.get(0).getPrice());

        Assertions.assertEquals(USERNAME, offerDTOList.get(1).getUserDTO().getUsername());
        Assertions.assertEquals(mockSummonEntity2.getName(), offerDTOList.get(1).getSummonDTO().getName());
        Assertions.assertEquals(20, offerDTOList.get(1).getPrice());
    }

    @Test
    void testFailToGetOfferDTOListByUsername() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getAllOffersByUsername("SomeName");
                });
    }

    @Test
    void testGetOfferDTOListByTypeAndByNotUsername() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity1 = new SummonEntity("TestSummon1", 10, 15, mockUserEntity);
        SummonEntity mockSummonEntity2 = new SummonEntity("TestSummon2", 10, 15, mockUserEntity);

        OfferEntity mockOfferEntity1 = new OfferEntity(USERNAME, OfferType.TRADE, mockSummonEntity1, mockUserEntity);
        mockOfferEntity1.setPrice(0);

        OfferEntity mockOfferEntity2 = new OfferEntity(USERNAME, OfferType.TRADE, mockSummonEntity2, mockUserEntity);
        mockOfferEntity2.setPrice(0);

        List<OfferEntity> mockEntityList = new ArrayList<>();

        mockEntityList.add(mockOfferEntity1);
        mockEntityList.add(mockOfferEntity2);

        when(mockOfferRepository.findAllByOfferTypeAndUsernameNot(OfferType.TRADE, "OtherUser"))
                .thenReturn(Optional.of(mockEntityList));

        List<OfferDTO> offerDTOList = toTest.getAllByTypeAndByNotUser(OfferType.TRADE, "OtherUser");

        Assertions.assertEquals(2, offerDTOList.size());

        Assertions.assertEquals(USERNAME, offerDTOList.get(0).getUserDTO().getUsername());
        Assertions.assertEquals(mockSummonEntity1.getName(), offerDTOList.get(0).getSummonDTO().getName());
        Assertions.assertEquals(0, offerDTOList.get(0).getPrice());

        Assertions.assertEquals(USERNAME, offerDTOList.get(1).getUserDTO().getUsername());
        Assertions.assertEquals(mockSummonEntity2.getName(), offerDTOList.get(1).getSummonDTO().getName());
        Assertions.assertEquals(0, offerDTOList.get(1).getPrice());
    }

    @Test
    void testFailToGetOfferDTOListByTypeByAndByNotUsername() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getAllByTypeAndByNotUser(OfferType.TRADE, USERNAME);
                });
    }
}
