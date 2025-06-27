package org.example.doctorplus.repo;

import org.example.doctorplus.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll(Sort sort);
    boolean existsByDoctor(Doctor doctor);
    boolean existsByServing(Serving serving);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByUser(User user);
    List<Appointment> findByPatient(User user);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
}