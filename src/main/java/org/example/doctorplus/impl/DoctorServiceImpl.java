package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.repo.AppointmentRepo;
import org.example.doctorplus.repo.DoctorRepo;
import org.example.doctorplus.service.DoctorService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepo.findById(id).orElse(null);
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepo.findById(id).orElse(null);
        if (doctor == null) return;

        if (appointmentRepo.existsByDoctor(doctor)) {
            throw new IllegalStateException("Доктор имеет связь с приемом.");
        }

        doctorRepo.delete(doctor);
    }

    @Override
    public List<Doctor> deleteAllExceptWithAppointments() {
        List<Doctor> allDoctors = doctorRepo.findAll();
        List<Doctor> undeleted = new ArrayList<>();

        for (Doctor doctor : allDoctors) {
            if (appointmentRepo.existsByDoctor(doctor)) {
                undeleted.add(doctor);
            } else {
                doctorRepo.delete(doctor);
            }
        }

        return undeleted;
    }

    // ✅ Методы для сортировки — нужно реализовать
    @Override
    public List<Doctor> findAllOrderByNameAsc() {
        return doctorRepo.findAllByOrderByNameAsc();
    }

    @Override
    public List<Doctor> findAllOrderByNameDesc() {
        return doctorRepo.findAllByOrderByNameDesc();
    }

    @Override
    public List<Doctor> findAllBySpecialityAsc() {
        return doctorRepo.findAllBySpecialityAsc();
    }

    @Override
    public List<Doctor> findAllBySpecialityDesc() {
        return doctorRepo.findAllBySpecialityDesc();
    }
}