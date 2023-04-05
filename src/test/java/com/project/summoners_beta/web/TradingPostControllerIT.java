package com.project.summoners_beta.web;

import com.project.summoners_beta.model.entities.OfferEntity;
import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
import com.project.summoners_beta.model.enums.OfferType;
import com.project.summoners_beta.repository.OfferRepository;
import com.project.summoners_beta.repository.SummonRepository;
import com.project.summoners_beta.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TradingPostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SummonRepository summonRepository;

    @Autowired
    private OfferRepository offerRepository;

    @BeforeEach
    void setUp() {
        offerRepository.deleteAll();
        summonRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testGetTradeOffersPage() throws Exception {

        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        mockMvc.perform(get("/trading-post/offers/trade")
                ).andDo(print())
                .andExpect(view().name("trading-page"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testGetSellOffersPage() throws Exception {

        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        mockMvc.perform(get("/trading-post/offers/sell")
                ).andDo(print())
                .andExpect(view().name("sales-page"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testCreateTradeOffer() throws Exception {
        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        SummonEntity card = new SummonEntity("Card", 20, 15, userEntity);
        summonRepository.saveAndFlush(card);

        mockMvc.perform(post("/trading-post/offers/trade?create")
                        .param("summonId", String.valueOf(card.getId()))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-offers"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testCreateSellOffer() throws Exception {
        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        SummonEntity card = new SummonEntity("Card", 20, 15, userEntity);
        summonRepository.saveAndFlush(card);

        mockMvc.perform(post("/trading-post/offers/sell?create")
                        .param("summonId", String.valueOf(card.getId()))
                        .param("price", String.valueOf(20))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-offers"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testAcceptTradeOffer() throws Exception {
        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        SummonEntity card = new SummonEntity("Card", 20, 15, userEntity);
        summonRepository.saveAndFlush(card);

        SummonEntity offerCard = new SummonEntity("OfferCard", 20, 15, userEntity);
        summonRepository.saveAndFlush(offerCard);

        OfferEntity offerEntity = new OfferEntity("TestUser", OfferType.TRADE, offerCard, userEntity);
        offerRepository.saveAndFlush(offerEntity);

        mockMvc.perform(post("/trading-post/offers/trade")
                        .param("summonId", String.valueOf(card.getId()))
                        .param("offerId", String.valueOf(offerEntity.getId()))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/roster"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testAcceptSellOffer() throws Exception {

        offerRepository.deleteAll();

        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        SummonEntity offerCard = new SummonEntity("OfferCard", 20, 15, userEntity);
        summonRepository.saveAndFlush(offerCard);

        OfferEntity offerEntity = new OfferEntity("TestUser", OfferType.SELL, offerCard, userEntity);
        offerEntity.setPrice(20);
        offerRepository.saveAndFlush(offerEntity);

        mockMvc.perform(post("/trading-post/offers/sell")
                        .param("offerId", String.valueOf(offerEntity.getId()))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/roster"));
    }
}
