package com.example.DoctorPlus.service;

import com.example.DoctorPlus.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAll();
    List<Patient> findAllSortedByField(String fieldName, String sortDirection);
    Optional<Patient> findById(Long id);
    void save(Patient patient);
    void deleteById(Long id);
    void deleteAll();
    boolean deleteByIdIfPossible(Long id);
    int deleteAllExceptWithAppointments();
}