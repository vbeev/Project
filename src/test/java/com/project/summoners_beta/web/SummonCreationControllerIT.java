package com.project.summoners_beta.web;

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
public class SummonCreationControllerIT {

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
    void testGetSummonCreationPage() throws Exception {
        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        mockMvc.perform(get("/summons/creation")
                ).andDo(print())
                .andExpect(view().name("create-page"));
    }

    @Test
    @WithMockUser(username = "TestUser", password = "testPass")
    void testCreateSummon() throws Exception {
        UserEntity userEntity = new UserEntity("TestUser", "testPass", "test@mail.com");
        userEntity.setCoins(50);
        userRepository.saveAndFlush(userEntity);

        mockMvc.perform(post("/summons/creation")
                        .param("name", "CardName")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/roster"));
    }
}
