package com.example.DoctorPlus.repo;

import com.example.DoctorPlus.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    List<Patient> findAllByOrderBySurnameAsc();
    List<Patient> findAllByOrderBySurnameDesc();
    List<Patient> findAllByOrderByNameAsc();
    List<Patient> findAllByOrderByNameDesc();
}