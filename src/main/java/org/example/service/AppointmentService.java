package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AppointmentDTO;
import org.example.dto.DoctorDTO;
import org.example.dto.PatientDTO;
import org.example.dto.ServiceDTO;
import org.example.model.Appointment;
import org.example.repo.AppointmentRepository;
import org.example.repo.DoctorRepository;
import org.example.repo.PatientRepository;
import org.example.repo.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final ServiceRepository serviceRepo;

    public void create(AppointmentDTO dto) {
        Appointment appointment = new Appointment();
        appointment.setDateTime(dto.getDateTime());

        appointment.setPatient(patientRepo.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Пациент не найден")));

        appointment.setDoctor(doctorRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Врач не найден")));

        appointment.setService(serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена")));

        appointmentRepo.save(appointment);
    }

    public List<AppointmentDTO> findAllSorted(String sort) {
        List<Appointment> appointments = switch (sort) {
            case "dateTime" -> appointmentRepo.findAllByOrderByDateTimeAsc();
            case "patient.name" -> appointmentRepo.findAllByOrderByPatient_NameAsc();
            case "doctor.name" -> appointmentRepo.findAllByOrderByDoctor_NameAsc();
            case "service.name" -> appointmentRepo.findAllByOrderByService_NameAsc();
            default -> appointmentRepo.findAll();
        };
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AppointmentDTO convertToDto(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getDateTime(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getService().getId()
        );
    }

    public void update(AppointmentDTO dto) {
        Appointment appointment = appointmentRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));

        appointment.setDateTime(dto.getDateTime());

        appointment.setPatient(patientRepo.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Пациент не найден")));

        appointment.setDoctor(doctorRepo.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Врач не найден")));

        appointment.setService(serviceRepo.findById(dto.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена")));

        appointmentRepo.save(appointment);
    }

    public void delete(Long id) {
        appointmentRepo.deleteById(id);
    }

    public AppointmentDTO findById(Long id) {
        return appointmentRepo.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("Запись не найдена"));
    }

    public List<PatientDTO> findAllPatients() {
        return patientRepo.findAll().stream()
                .map(p -> new PatientDTO())
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> findAllDoctors() {
        return doctorRepo.findAll().stream()
                .map(d -> new DoctorDTO())
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> findAllServices() {
        return serviceRepo.findAll().stream()
                .map(s -> new ServiceDTO())
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> findAll() {
        return appointmentRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

}