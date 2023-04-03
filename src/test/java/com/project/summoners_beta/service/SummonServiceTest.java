package com.project.summoners_beta.service;

import com.project.summoners_beta.exceptions.NotEnoughCoinsException;
import com.project.summoners_beta.exceptions.ObjectNotFoundException;
import com.project.summoners_beta.model.dto.SummonCreationDTO;
import com.project.summoners_beta.model.dto.SummonDTO;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.repository.SummonRepository;
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
public class SummonServiceTest {

    private final String USERNAME = "TestUsername";

    @Mock
    private SummonRepository mockSummonRepository;

    @Mock
    private UserService mockUserService;

    @Captor
    private ArgumentCaptor<SummonEntity> summonEntityArgumentCaptor;

    private SummonService toTest;

    @BeforeEach
    void setUp() {
        toTest = new SummonService(new ModelMapper(), mockUserService, mockSummonRepository);
    }

    @Test
    void testSummonCreation() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        SummonCreationDTO summonCreationDTO = new SummonCreationDTO("TestName");

        toTest.create(summonCreationDTO, USERNAME);

        Mockito.verify(mockSummonRepository).saveAndFlush(summonEntityArgumentCaptor.capture());

        SummonEntity savedSummonEntity = summonEntityArgumentCaptor.getValue();

        Assertions.assertEquals(summonCreationDTO.getName(), savedSummonEntity.getName());
        Assertions.assertEquals(75, mockUserEntity.getCoins());
    }

    @Test
    void testSummonCreationShouldFailIfNotEnoughCoins() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        mockUserEntity.setCoins(0);

        when(mockUserService.getUserByUsername(USERNAME)).thenReturn(mockUserEntity);

        SummonCreationDTO summonCreationDTO = new SummonCreationDTO("TestName");

        assertThrows(NotEnoughCoinsException.class,
                () -> {
                    toTest.create(summonCreationDTO, USERNAME);
                });
    }

    @Test
    void testGetSummonDTOById() {

        SummonEntity mockSummonEntity = new SummonEntity("TestName", 10, 15, null);

        when(mockSummonRepository.findById(1L)).thenReturn(Optional.of(mockSummonEntity));

        SummonDTO summonDTO = toTest.getById(1L);

        Assertions.assertEquals("TestName", summonDTO.getName());
        Assertions.assertEquals(10, summonDTO.getHp());
        Assertions.assertEquals(15, summonDTO.getAtk());
    }

    @Test
    void testFailToGetSummonDTOById() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getById(2L);
                });
    }

    @Test
    void testGetSummonEntityById() {

        SummonEntity mockSummonEntity = new SummonEntity("TestName", 10, 15, null);

        when(mockSummonRepository.findById(1L)).thenReturn(Optional.of(mockSummonEntity));

        SummonEntity testEntity = toTest.getSummonById(1L);

        Assertions.assertEquals("TestName", testEntity.getName());
        Assertions.assertEquals(10, testEntity.getHp());
        Assertions.assertEquals(15, testEntity.getAtk());
    }

    @Test
    void testFailToGetSummonEntityById() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getSummonById(2L);
                });
    }

    @Test
    void testGetSummonDTOListByUsername() {

        UserEntity mockUserEntity = new UserEntity(USERNAME, "testPass", "test@mail.com");

        SummonEntity mockSummonEntity1 = new SummonEntity("TestName1", 10, 15, mockUserEntity);
        SummonEntity mockSummonEntity2 = new SummonEntity("TestName2", 10, 15, mockUserEntity);

        List<SummonEntity> mockEntityList = new ArrayList<>();

        mockEntityList.add(mockSummonEntity1);
        mockEntityList.add(mockSummonEntity2);

        when(mockSummonRepository.findAllByUser_Username(USERNAME)).thenReturn(Optional.of(mockEntityList));

        List<SummonDTO> summonDTOList = toTest.getAllByUsername(USERNAME);

        Assertions.assertEquals(2, summonDTOList.size());

        Assertions.assertEquals("TestName1", summonDTOList.get(0).getName());
        Assertions.assertEquals(10, summonDTOList.get(0).getHp());
        Assertions.assertEquals(15, summonDTOList.get(0).getAtk());

        Assertions.assertEquals("TestName2", summonDTOList.get(1).getName());
    }

    @Test
    void testFailToGetSummonDTOListByUsername() {
        assertThrows(ObjectNotFoundException.class,
                () -> {
                    toTest.getAllByUsername("SomeName");
                });
    }
}
