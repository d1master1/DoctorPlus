package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.ServingService;
import com.example.DoctorPlus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfileControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final UserService userService = mock(UserService.class);
    private final ServingService servingService = mock(ServingService.class);
    private final AppointmentService appointmentService = mock(AppointmentService.class);

    // Тестовые данные
    private User testUser;
    private Appointment testAppointment;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setName("Иван");
        testUser.setSurname("Иванов");

        testAppointment = new Appointment();
        testAppointment.setId(1L);

        // Создаём контроллер с внедрением зависимостей вручную
        ProfileController controller = new ProfileController(
                userService, servingService, appointmentService
        );

        // Настраиваем MockMvc для standalone-тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testUserProfile_ReturnsProfilePage() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(appointmentService.findAllByUser(testUser)).thenReturn(singletonList(testAppointment));

        mockMvc.perform(get("/profile")
                        .principal(() -> "testuser"))
                .andExpect(status().isOk())
                .andExpect(view().name("include/profile"))
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("appointmentList", singletonList(testAppointment)));
    }

    @Test
    void testUploadAvatar_EmptyFile_NoAction() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "avatar",
                "",
                "image/jpeg",
                new byte[0]
        );

        mockMvc.perform(multipart("/profile/avatar")
                        .file(emptyFile)
                        .principal(() -> "testuser"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        verify(userService, never()).save(any(User.class));
    }
}