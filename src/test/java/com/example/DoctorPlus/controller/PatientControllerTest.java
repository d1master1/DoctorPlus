package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {

    private MockMvc mockMvc;

    // Мокаем зависимости
    private final PatientService patientService = mock(PatientService.class);

    // Тестовые данные
    private List<Patient> testPatients;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        // Инициализируем тестовые данные
        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setName("Алексей");
        testPatient.setSurname("Смирнов");
        testPatient.setPhone("+79998887766");

        testPatients = singletonList(testPatient);

        // Создаём контроллер с внедрением зависимостей вручную
        PatientController controller = new PatientController(patientService);

        // Настраиваем MockMvc для standalone-тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testListPatients_ReturnsViewWithPatients() throws Exception {
        when(patientService.findAll()).thenReturn(testPatients);

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patient_list"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", testPatients));
    }

    @Test
    void testListPatients_WithSort_ReturnsSorted() throws Exception {
        when(patientService.findAllSortedByField("lastname", "asc")).thenReturn(testPatients);

        mockMvc.perform(get("/patient")
                        .param("sortField", "lastName")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patient_list"))
                .andExpect(model().attribute("patients", testPatients))
                .andExpect(model().attribute("sortField", "lastName"))
                .andExpect(model().attribute("sortDir", "asc"));
    }

    @Test
    void testShowAddForm_ReturnsFormView() throws Exception {
        mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patient_form"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void testEditPatientForm_ExistingId_ReturnsFormWithPatient() throws Exception {
        when(patientService.findById(1L)).thenReturn(Optional.of(testPatient));

        mockMvc.perform(get("/patient/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/patient_form"))
                .andExpect(model().attribute("patient", testPatient));
    }

    @Test
    void testEditPatientForm_NonExistingId_RedirectsToList() throws Exception {
        when(patientService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/patient/edit/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient"));
    }

    @Test
    void testSavePatient_ValidData_RedirectsToList() throws Exception {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Алексей");
        patient.setSurname("Смирнов");
        patient.setPhone("+79998887766");

        mockMvc.perform(post("/patient/save")
                        .flashAttr("patient", patient))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient"));

        verify(patientService, times(1)).save(patient);
    }

    @Test
    void testDeleteAllPatients_RedirectsAfterDeletion() throws Exception {
        when(patientService.deleteAllExceptWithAppointments()).thenReturn(2); // 2 не удалено

        mockMvc.perform(post("/patient/deleteAll"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient?partialError=2"));

        verify(patientService, times(1)).deleteAllExceptWithAppointments();
    }
}