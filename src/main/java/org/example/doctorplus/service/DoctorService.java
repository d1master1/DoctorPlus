package org.example.doctorplus.service;

import org.example.doctorplus.model.Doctor;
import java.util.List;

public interface DoctorService {

    Doctor getDoctorById(Long id);
    void saveDoctor(Doctor doctor);
    void deleteDoctor(Long id);
    List<Doctor> findAll();
    List<Doctor> deleteAllExceptWithAppointments();

    List<Doctor> findAllOrderByNameAsc();
    List<Doctor> findAllOrderByNameDesc();
    List<Doctor> findAllBySpecialityAsc();
    List<Doctor> findAllBySpecialityDesc();
}