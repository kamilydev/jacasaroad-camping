package com.camping.jacasaroad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JacasaroadApplication.class)
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void loginWithValidUser() throws Exception {
        mockMvc.perform(formLogin("/login").user("marciosilva").password("senhaSegura"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }


    @Test
    public void loginWithInvalidUser() throws Exception {
        mockMvc.perform(formLogin("/login").user("invalid").password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}

