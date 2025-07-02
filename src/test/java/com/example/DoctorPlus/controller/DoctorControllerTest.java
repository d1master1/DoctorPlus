package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.service.DoctorService;
import com.example.DoctorPlus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DoctorControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final DoctorService doctorService = mock(DoctorService.class);
    private final UserService userService = mock(UserService.class);

    // Тестовые данные
    private List<Doctor> testDoctors;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        testDoctors = singletonList(new Doctor());
        testUsers = singletonList(new User());

        // Создаём контроллер с внедрением зависимостей вручную
        DoctorController controller = new DoctorController(doctorService, userService);

        // Настраиваем MockMvc для standalone-тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListDoctors_ReturnsViewWithDoctors() throws Exception {
        when(doctorService.getAllDoctorsWithUsers()).thenReturn(testDoctors);

        mockMvc.perform(get("/doctor"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/doctor_list"))
                .andExpect(model().attributeExists("doctors"))
                .andExpect(model().attribute("doctors", testDoctors));
    }

    @Test
    void testShowAddForm_ReturnsFormView() throws Exception {
        when(userService.findAll()).thenReturn(testUsers);

        mockMvc.perform(get("/doctor/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/doctor_form"))
                .andExpect(model().attributeExists("doctor"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testSaveDoctor_ValidData_RedirectsToList() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Петр");
        doctor.setSurname("Петров");
        doctor.setPatronymic("Петрович");
        doctor.setUser(testUsers.getFirst());

        mockMvc.perform(post("/doctor/save")
                        .flashAttr("doctor", doctor))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor"));

        verify(doctorService, times(1)).saveDoctor(doctor);
    }

    @Test
    void testEditDoctor_ExistingId_ReturnsFormWithDoctor() throws Exception {
        Doctor doctor = testDoctors.get(0);
        when(doctorService.getDoctorById(1L)).thenReturn(doctor);
        when(userService.findAll()).thenReturn(testUsers);

        mockMvc.perform(get("/doctor/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor/doctor_form"))
                .andExpect(model().attribute("doctor", doctor));
    }

    @Test
    void testDeleteDoctor_DeletesAndRedirects() throws Exception {
        mockMvc.perform(get("/doctor/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor"));

        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    void testGetPatronymicByUserId_ReturnsJsonResponse() throws Exception {
        Doctor doctor = testDoctors.get(0);
        doctor.setPatronymic("Иванович");
        when(doctorService.getDoctorByUserId(1L)).thenReturn(doctor);

        mockMvc.perform(get("/doctor/patronymic/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.patronymic").value("Иванович"));
    }
}