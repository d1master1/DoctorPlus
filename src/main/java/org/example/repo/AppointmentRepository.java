package org.example.repo;

import org.example.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();
    List<Appointment> findAllByOrderByDateTimeAsc();
    List<Appointment> findAllByOrderByPatient_NameAsc();
    List<Appointment> findAllByOrderByDoctor_NameAsc();
    List<Appointment> findAllByOrderByService_NameAsc();
}