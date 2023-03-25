package com.project.summoners_beta.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "summons")
public class SummonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int atk;

    @ManyToOne
    private UserEntity user;

    public SummonEntity() {
    }

    public SummonEntity(String name, int hp, int atk, UserEntity user) {
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.user = user;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
