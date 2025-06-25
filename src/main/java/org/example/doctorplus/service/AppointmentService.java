package org.example.doctorplus.service;

import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {

    List<Appointment> getAllAppointments(Sort sort);
    void saveAppointment(Appointment appointment);
    void deleteAppointment(Long id);
    List<Appointment> findAll();
    Optional<Appointment> findById(Long id);
    void deleteAllAppointments();
    boolean existsByServingId(Long id);
    List<Appointment> findByPatient(Patient patient);
}