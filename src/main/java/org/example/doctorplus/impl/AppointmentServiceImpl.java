package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.*;
import org.example.doctorplus.repo.AppointmentRepo;
import org.example.doctorplus.repo.DoctorRepo;
import org.example.doctorplus.repo.PatientRepo;
import org.example.doctorplus.repo.ServingRepo;
import org.example.doctorplus.service.AppointmentService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepository;
    private final PatientRepo patientRepository;
    private final DoctorRepo doctorRepository;
    private final ServingRepo servingRepository;

    @Override
    public List<Appointment> getAllAppointments(Sort sort) {
        return appointmentRepository.findAll(sort);
    }

    @Override
    public void saveAppointment(Appointment appointment) {
        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            Patient patient = patientRepository.findById(appointment.getPatient().getId()).orElse(null);
            appointment.setPatient(patient);
        }
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
            appointment.setDoctor(doctor);
        }
        if (appointment.getServing() != null && appointment.getServing().getId() != null) {
            Serving serving = servingRepository.findById(appointment.getServing().getId()).orElse(null);
            appointment.setServing(serving);
        }
        appointmentRepository.save(appointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
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
    public boolean existsByServingId(Long id) {
        return false;
    }

    @Override
    public List<Appointment> findByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }
}