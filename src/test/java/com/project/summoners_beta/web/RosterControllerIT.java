package com.project.summoners_beta.web;

import com.project.summoners_beta.model.entities.SummonEntity;
import com.project.summoners_beta.model.entities.UserEntity;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RosterControllerIT {

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
    void testPostSelectedCard() throws Exception {
        mockMvc.perform(post("/roster")
                        .param("cardId", String.valueOf(1L))
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/arena"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testGetMainPage() throws Exception {

        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        SummonEntity card1 = new SummonEntity("Card1", 20, 15, userEntity);
        summonRepository.saveAndFlush(card1);

        SummonEntity card2 = new SummonEntity("Card2", 25, 10, userEntity);
        summonRepository.saveAndFlush(card2);

        mockMvc.perform(get("/roster")
                ).andDo(print())
                .andExpect(view().name("roster-page"));
    }
}
