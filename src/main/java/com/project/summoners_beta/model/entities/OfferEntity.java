package com.project.summoners_beta.model.entities;

import com.project.summoners_beta.model.enums.OfferType;
import jakarta.persistence.*;

@Entity
@Table(name = "offers")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column
    private int price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferType offerType;

    @OneToOne
    private SummonEntity summonEntity;

    @ManyToOne
    private UserEntity user;

    public OfferEntity() {
    }

    public OfferEntity(String username, OfferType offerType, SummonEntity summonEntity, UserEntity user) {
        this.username = username;
        this.offerType = offerType;
        this.summonEntity = summonEntity;
        this.user = user;
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
    public OfferType getOfferType() {
        return offerType;
    }
    public void setOfferType(OfferType offerType) {
        this.offerType = offerType;
    }
    public SummonEntity getSummonEntity() {
        return summonEntity;
    }
    public void setSummonEntity(SummonEntity summonEntity) {
        this.summonEntity = summonEntity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
