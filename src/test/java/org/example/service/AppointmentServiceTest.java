package org.example.service;

import org.example.dto.AppointmentDTO;
import org.example.model.*;
import org.example.repo.AppointmentRepository;
import org.example.repo.DoctorRepository;
import org.example.repo.PatientRepository;
import org.example.repo.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepo;

    @Mock
    private PatientRepository patientRepo;

    @Mock
    private DoctorRepository doctorRepo;

    @Mock
    private ServiceRepository serviceRepo;

    @InjectMocks
    private AppointmentService appointmentService;

    private AppointmentDTO dto;

    @BeforeEach
    void setUp() {
        dto = AppointmentDTO.builder()
                .dateTime(LocalDateTime.now())
                .patientId(1L)
                .doctorId(2L)
                .serviceId(3L)
                .build();
    }

    @Test
    void testCreateAppointment_success() {
        when(patientRepo.findById(1L)).thenReturn(java.util.Optional.of(new Patient()));
        when(doctorRepo.findById(2L)).thenReturn(java.util.Optional.of(new Doctor()));
        when(serviceRepo.findById(3L)).thenReturn(java.util.Optional.of(new Service()));

        assertDoesNotThrow(() -> appointmentService.create(dto));
    }

    @Test
    void testCreateAppointment_patientNotFound() {
        when(patientRepo.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.create(dto));
    }
}