package org.example.doctorplus.service;

import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.model.User;
import org.springframework.data.domain.Sort;
import java.util.List;

public interface DoctorService {

    List<Doctor> getAllDoctorsSortedBy(String sortBy);
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