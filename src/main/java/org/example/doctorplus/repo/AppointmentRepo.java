package org.example.doctorplus.repo;

import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll(Sort sort);
    boolean existsByPatient(Patient patient);
    boolean existsByServing(Serving serving);
    boolean existsByDoctor(Doctor doctor);
    List<Appointment> findByPatient(Patient patient);
}