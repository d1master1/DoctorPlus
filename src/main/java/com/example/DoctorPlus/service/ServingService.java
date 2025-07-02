package com.example.DoctorPlus.service;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.model.User;

import java.util.List;

public interface ServingService {
    Serving getServingById(Long id);
    Serving saveServing(Serving realty);
    void deleteServing(Long id);
    List<Serving> findAll();
    void deleteAll();
    Doctor findByUser(User user);
    /*List<Serving> findAllByOwner(User owner);*/
    List<Serving> findAllByPatientUser(User user);
    void deleteById(Long id);
    int deleteAllExceptInAppointments();
    int deleteUnlinkedServings();
    int countLinkedServings();
    List<Serving> deleteAllExceptWithAppointments();
    boolean existsByServingId(Long id);
}