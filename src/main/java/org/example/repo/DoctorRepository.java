package org.example.repo;

import org.example.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAll();
    List<Doctor> findAllByOrderByNameAsc();
    List<Doctor> findAllByOrderBySpecialtyAsc();
}