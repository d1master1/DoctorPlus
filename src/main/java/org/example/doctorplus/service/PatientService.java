package org.example.doctorplus.service;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;

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
    Optional<Patient> findByUser(User user);
}