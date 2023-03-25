package com.project.summoners_beta.model.dto;

public class ConfirmOfferDTO {
    private Long offerId;

    public ConfirmOfferDTO(Long offerId) {
        this.offerId = offerId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }
}
