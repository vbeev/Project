package com.project.summoners_beta.model.dto;

public class SaleOfferDTO {

    private int price;

    public SaleOfferDTO(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
