package org.example.doctorplus.service;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface ServingService {
    List<Serving> findAll(Sort sort);
    Optional<Serving> findById(Long id);
    Serving save(Serving serving);
    List<Serving> deleteAllExceptWithAppointments();
    Serving getServingById(Long id);
    void saveServing(Serving serving);
    List<Serving> findAll();
    List<Serving> findAllSorted(Sort sort);
    boolean deleteByIdIfPossible(Long id);
    List<Serving> findAllByUser(User user);
    List<Serving> findAllSorted(String sortField, String sortDir);
    List<Serving> findByPatient(Patient patient);
    void deleteServing(Long id);
    void deleteAll();
}