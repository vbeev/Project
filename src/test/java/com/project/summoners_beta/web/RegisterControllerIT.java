package com.project.summoners_beta.web;

import com.project.summoners_beta.repository.OfferRepository;
import com.project.summoners_beta.repository.SummonRepository;
import com.project.summoners_beta.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerIT {

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
    void testRegistration() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("email", "test@mail.com")
                .param("username", "TestName")
                .param("password", "testPass")
                .with(csrf())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/users/register")
                ).andDo(print())
                .andExpect(view().name("register-page"));
    }
}
