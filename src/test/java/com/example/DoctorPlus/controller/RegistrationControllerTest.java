package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.dto.UserDTO;
import com.example.DoctorPlus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegistrationControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final UserService userService = mock(UserService.class);

    // Тестовые данные
    private UserDTO validUserDTO;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        validUserDTO = new UserDTO();
        validUserDTO.setUsername("testuser");
        validUserDTO.setPassword("password123");
        validUserDTO.setConfirmPassword("password123");

        // Создаём контроллер с внедрением зависимостей вручную
        RegistrationController controller = new RegistrationController(userService);

        // Настраиваем MockMvc для standalone-тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testShowRegistrationForm_ReturnsRegistrationView() throws Exception {
        mockMvc.perform(get("/include/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("include/registration"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    void testRegisterUser_InvalidData_ShowErrors() throws Exception {
        UserDTO invalidUserDTO = new UserDTO();
        invalidUserDTO.setUsername(""); // Невалидное имя
        invalidUserDTO.setPassword("pass");
        invalidUserDTO.setConfirmPassword("pass123");

        mockMvc.perform(post("/include/registration/save")
                        .flashAttr("userDTO", invalidUserDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("include/registration"))
                .andExpect(model().hasErrors());
    }
}