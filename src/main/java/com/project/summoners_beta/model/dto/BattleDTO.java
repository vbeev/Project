package com.project.summoners_beta.model.dto;

public class BattleDTO {

    private Long userCardId;

    private  Long enemyCardId;

    public BattleDTO() {
    }

    public BattleDTO(Long userCardId, Long enemyCardId) {
        this.userCardId = userCardId;
        this.enemyCardId = enemyCardId;
    }

    public Long getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(Long userCardId) {
        this.userCardId = userCardId;
    }

    public Long getEnemyCardId() {
        return enemyCardId;
    }

    public void setEnemyCardId(Long enemyCardId) {
        this.enemyCardId = enemyCardId;
    }
}
