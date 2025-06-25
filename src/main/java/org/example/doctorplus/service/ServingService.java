package org.example.doctorplus.service;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import java.util.List;

public interface ServingService {

    static List<Serving> findAllByOwner(User user) {
        return null;
    }
    List<Serving> findAll();
    Serving getServingById(Long id);
    Serving saveServing(Serving serving);
    void deleteServing(Long id);
    void deleteAll();
    List<Serving> findByPatient(Patient patient);
    List<Serving> deleteAllExceptWithAppointments();
}