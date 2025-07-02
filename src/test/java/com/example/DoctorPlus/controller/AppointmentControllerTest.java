package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.DoctorService;
import com.example.DoctorPlus.service.PatientService;
import com.example.DoctorPlus.service.ServingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AppointmentControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final AppointmentService appointmentService = mock(AppointmentService.class);
    private final PatientService patientService = mock(PatientService.class);
    private final DoctorService doctorService = mock(DoctorService.class);
    private final ServingService servingService = mock(ServingService.class);

    // Тестовые данные
    private List<Appointment> testAppointments;
    private List<Patient> testPatients;
    private List<Doctor> testDoctors;
    private List<Serving> testServings;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        testAppointments = singletonList(new Appointment());
        testPatients = singletonList(new Patient());
        testDoctors = singletonList(new Doctor());
        testServings = singletonList(new Serving());

        // Создаём контроллер с моками
        AppointmentController controller = new AppointmentController(
                appointmentService, patientService, servingService, doctorService
        );

        // Настраиваем MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListAppointments_ReturnsViewWithAppointments() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(testAppointments);
        when(patientService.findAll()).thenReturn(testPatients);
        when(doctorService.findAll()).thenReturn(testDoctors);
        when(servingService.findAll()).thenReturn(testServings);

        mockMvc.perform(get("/appointment"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointment/appointment_list"))
                .andExpect(model().attributeExists("appointments"))
                .andExpect(model().attribute("appointments", testAppointments));
    }

    @Test
    void testShowAddForm_ReturnsFormViewWithAttributes() throws Exception {
        when(patientService.findAll()).thenReturn(testPatients);
        when(doctorService.findAll()).thenReturn(testDoctors);
        when(servingService.findAll()).thenReturn(testServings);

        mockMvc.perform(get("/appointment/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointment/appointment_form"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attributeExists("servings"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attributeExists("doctors"))
                .andExpect(model().attributeExists("servingJson"))
                .andExpect(model().attributeExists("patientJson"));
    }

    @Test
    void testDeleteAppointment_RedirectsToList() throws Exception {
        mockMvc.perform(post("/appointment/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));

        verify(appointmentService, times(1)).deleteAppointmentById(1L);
    }

    @Test
    void testDeleteAllAppointments_RedirectsToList() throws Exception {
        mockMvc.perform(post("/appointment/deleteAll"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));

        verify(appointmentService, times(1)).deleteAllAppointments();
    }
}