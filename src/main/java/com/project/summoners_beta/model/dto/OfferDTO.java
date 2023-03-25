package com.project.summoners_beta.model.dto;

public class OfferDTO {
    private Long id;
    private String username;
    private int price;
    private SummonDTO summonDTO;
    private UserDTO userDTO;

    public OfferDTO() {
    }

    public OfferDTO(String username, SummonDTO summonDTO, UserDTO userDTO) {
        this.username = username;
        this.summonDTO = summonDTO;
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public SummonDTO getSummonDTO() {
        return summonDTO;
    }

    public void setSummonDTO(SummonDTO summonDTO) {
        this.summonDTO = summonDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
