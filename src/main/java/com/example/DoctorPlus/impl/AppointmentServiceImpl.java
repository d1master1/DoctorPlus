package com.example.DoctorPlus.impl;

import com.example.DoctorPlus.model.*;
import com.example.DoctorPlus.repo.AppointmentRepo;
import com.example.DoctorPlus.repo.DoctorRepo;
import com.example.DoctorPlus.repo.PatientRepo;
import com.example.DoctorPlus.repo.ServingRepo;
import com.example.DoctorPlus.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepository;
    private final ServingRepo servingRepository;
    private final PatientRepo patientRepository;
    private final DoctorRepo doctorRepository;

    @Override
    public boolean existsById(Long id) {
        return appointmentRepository.existsById(id);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getServing() != null && appointment.getServing().getId() != null) {
            Serving serving = servingRepository.findById(appointment.getServing().getId()).orElse(null);
            appointment.setServing(serving);
        }

        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(appointment.getPatient().getId()).orElse(null);
            appointment.setPatient(patient);
        }

        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
            appointment.setDoctor(doctor);
        }

        appointmentRepository.save(appointment);
        return appointment;
    }

    @Override
    public void deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
        }
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> findAllByUser(User user) {
        return appointmentRepository.findAllByUser(user);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public void deleteAllAppointments() {
        appointmentRepository.deleteAll();
    }

    @Override
    public boolean existsByServingId(Long servingId) {
        return appointmentRepository.existsByServingId(servingId);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public void deleteAppointmentsByIds(List<Long> ids) {
        List<Appointment> dealsToDelete = appointmentRepository.findAllById(ids);
        appointmentRepository.deleteAll(dealsToDelete);
    }
}