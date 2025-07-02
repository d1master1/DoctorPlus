package com.example.DoctorPlus.repo;

import com.example.DoctorPlus.model.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    List<Appointment> findAll(Sort sort);
    @Query("SELECT d FROM Appointment d WHERE d.patient.user = :user")
    List<Appointment> findAllByUser(@Param("user") User user);
    boolean existsByPatient(Patient patient);
    boolean existsByDoctor(Doctor doctor);
    boolean existsByServingId(Long servingId);
    List<Appointment> findByServing(Serving serving);
}