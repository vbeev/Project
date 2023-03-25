package com.project.summoners_beta.model.dto;

public class TradeOfferDTO {
    private Long summonId;

    private Integer price;


    public TradeOfferDTO(Long summonId, Integer price) {
        this.summonId = summonId;
        this.price = price;

    }

    public Long getSummonId() {
        return summonId;
    }

    public void setSummonId(Long summonId) {
        this.summonId = summonId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
