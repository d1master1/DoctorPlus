package org.example.repo;

import org.example.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAll();
    Optional<Patient> findById(Long id);
    boolean existsById(Long id);
    List<Patient> findAllByOrderByNameAsc();
    List<Patient> findAllByOrderByEmailAsc();
}