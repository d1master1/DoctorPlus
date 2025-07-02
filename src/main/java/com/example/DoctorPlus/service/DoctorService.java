package com.example.DoctorPlus.service;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctorsSortedBy(String sortBy);
    List<Doctor> getAllDoctorSortedBy(String sortBy);
    Doctor getDoctorById(Long id);
    void saveDoctor(Doctor doctor);
    void deleteDoctor(Long id);
    void deleteAllDoctors();
    List<Doctor> getAllDoctors();
    List<Doctor> getAllDoctorsSorted(Sort sort);
    Doctor findByUser(User user);
    List<Doctor> findAll();
    Doctor getDoctorByUserId(Long userId);
    List<Doctor> getAllDoctorsWithUsers();
    List<Doctor> deleteAllExceptWithAppointments();
}