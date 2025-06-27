package org.example.doctorplus.service;

import org.example.doctorplus.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> findAll();
    Optional<Patient> findById(Long id);
    void save(Patient patient);
    boolean deleteByIdIfPossible(Long id);
    List<Patient> findAllSortedByField(String field, String direction);
    int deleteAllExceptWithAppointments();
}