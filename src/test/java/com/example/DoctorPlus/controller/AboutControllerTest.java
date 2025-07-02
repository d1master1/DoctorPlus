package com.example.DoctorPlus.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AboutControllerTest {

    private MockMvc mockMvc;

    private AboutController aboutController;

    @BeforeEach
    void setUp() {
        aboutController = new AboutController(); // создаём контроллер напрямую
        mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();
    }

    @Test
    void testAboutPage() throws Exception {
        mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("include/about"))
                .andExpect(model().size(0));
    }
}