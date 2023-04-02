package com.project.summoners_beta.service;

import com.project.summoners_beta.model.dto.SummonDTO;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BattleService {
    private final SummonService summonService;

    public BattleService(SummonService summonService) {
        this.summonService = summonService;
    }

    public boolean battle(SummonDTO userCard, SummonDTO enemyCard) {

        int userHP = userCard.getHp();
        int userAtk = userCard.getAtk();

        int enemyHP = enemyCard.getHp();
        int enemyAtk = enemyCard.getAtk();

        do {
           enemyHP = enemyHP - userAtk;

           if (enemyHP <= 0) {
               return true;
           }

           userHP = userHP - enemyAtk;

        }
        while (userHP > 0);

        return false;
    }

    public SummonDTO getEnemyCard(String username) {

        return this.summonService.getRandomByNotUser(username);
    }
}
