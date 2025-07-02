package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.PatientService;
import com.example.DoctorPlus.service.ServingService;
import com.example.DoctorPlus.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServingControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final ServingService servingService = mock(ServingService.class);
    private final UserService userService = mock(UserService.class);
    private final AppointmentService appointmentService = mock(AppointmentService.class);
    private final PatientService patientService = mock(PatientService.class);

    // Тестовые данные
    private List<Serving> testServings;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        Serving serving = new Serving();
        serving.setId(1L);
        serving.setName("Анализ крови");
        serving.setDescription("Общий анализ");
        serving.setCost(300.0);

        testServings = Collections.singletonList(serving);

        // Создаём контроллер с внедрением зависимостей вручную
        ServingController controller = new ServingController(
                servingService, userService, appointmentService, patientService
        );

        // Настраиваем MockMvc для standalone-тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListServings_ReturnsViewWithServings() throws Exception {
        when(servingService.findAll()).thenReturn(testServings);

        mockMvc.perform(get("/serving"))
                .andExpect(status().isOk())
                .andExpect(view().name("serving/serving_list"))
                .andExpect(model().attributeExists("servings"))
                .andExpect(model().attribute("servings", testServings));
    }

    @Test
    void testListServings_WithSort_ReturnsSorted() throws Exception {
        when(servingService.findAll()).thenReturn(testServings);

        mockMvc.perform(get("/serving")
                        .param("sortField", "name")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("serving/serving_list"))
                .andExpect(model().attribute("servings", testServings))
                .andExpect(model().attribute("sortField", "name"))
                .andExpect(model().attribute("sortDir", "desc"));
    }

    @Test
    void testShowAddForm_ReturnsFormView() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
        when(appointmentService.findAll()).thenReturn(Collections.emptyList());
        when(patientService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/serving/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("serving/serving_form"))
                .andExpect(model().attributeExists("serving"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void testSaveServing_ValidData_RedirectsToList() throws Exception {
        Serving serving = new Serving();
        serving.setId(1L);
        serving.setName("Анализ крови");
        serving.setDescription("Общий анализ");
        serving.setCost(300.0);

        mockMvc.perform(post("/serving/save")
                        .flashAttr("serving", serving))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/serving"));

        verify(servingService, times(1)).saveServing(serving);
    }

    @Test
    void testEditServing_ExistingId_ReturnsFormWithServing() throws Exception {
        Serving serving = testServings.get(0);
        when(servingService.getServingById(1L)).thenReturn(serving);
        when(userService.findAll()).thenReturn(Collections.emptyList());
        when(appointmentService.findAll()).thenReturn(Collections.emptyList());
        when(patientService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/serving/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("serving/serving_form"))
                .andExpect(model().attribute("serving", serving));
    }

    @Test
    void testDeleteServing_DeletesAndRedirects() throws Exception {
        when(servingService.existsByServingId(1L)).thenReturn(false);

        mockMvc.perform(post("/serving/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/serving"));

        verify(servingService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteServing_UsedInAppointment_ShowsError() throws Exception {
        when(servingService.existsByServingId(1L)).thenReturn(true);

        mockMvc.perform(post("/serving/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/serving"));

        verify(servingService, never()).deleteById(1L);
    }

    @Test
    void testDeleteAllServings_RedirectsAfterDeletion() throws Exception {
        when(servingService.deleteAllExceptWithAppointments()).thenReturn(testServings);

        mockMvc.perform(post("/serving/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/serving"));

        verify(servingService, times(1)).deleteAllExceptWithAppointments();
    }
}