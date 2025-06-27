package org.example.doctorplus.repo;

import org.example.doctorplus.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll(Sort sort);
    boolean existsByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByUser(User user);
    boolean existsByServing(Serving serving);
    @Query("SELECT a.patient FROM Appointment a WHERE a.id = ?1")
    Optional<Patient> findPatientById(Long id);
    Optional<Appointment> findById(Long id);
}