package com.project.summoners_beta.service;

import com.project.summoners_beta.model.dto.SummonDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {

    @Mock
    private SummonService mockSummonService;

    private BattleService toTest;

    @BeforeEach
    void setUp() {
        toTest = new BattleService(mockSummonService);
    }

    @Test
    void testBattleWin() {
        SummonDTO mockUserCard = new SummonDTO("UserCard", 15, 20, null);

        SummonDTO mockEnemyCard = new SummonDTO("EnemyCard", 10, 15, null);

        boolean result = toTest.battle(mockUserCard, mockEnemyCard);

        Assertions.assertTrue(result);
    }

    @Test
    void testBattleLoss() {
        SummonDTO mockUserCard = new SummonDTO("UserCard", 15, 5, null);

        SummonDTO mockEnemyCard = new SummonDTO("EnemyCard", 20, 20, null);

        boolean result = toTest.battle(mockUserCard, mockEnemyCard);

        Assertions.assertFalse(result);
    }
}
