package com.example.DoctorPlus.service;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.User;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Appointment saveAppointment(Appointment appointment);
    void deleteAppointment(Long id);
    List<Appointment> findAll();
    List<Appointment> findAllByUser(User user);
    Optional<Appointment> findById(Long id);
    void deleteAllAppointments();
    boolean existsByServingId(Long id);
    void deleteAppointmentById(Long id);
    void deleteAppointmentsByIds(List<Long> ids);

    boolean existsById(Long id);
}