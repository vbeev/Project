package com.project.summoners_beta.model.dto;

public class SummonDTO {

    private Long id;

    private String name;

    private int hp;

    private int atk;

    private UserDTO userDTO;

    public SummonDTO() {
    }

    public SummonDTO(String name, int hp, int atk, UserDTO userDTO) {
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
