package com.project.summoners_beta.model.dto;

public class CardForBattleDTO {

    private Long cardId;

    public CardForBattleDTO() {
    }

    public CardForBattleDTO(Long cardId) {
        this.cardId = cardId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
