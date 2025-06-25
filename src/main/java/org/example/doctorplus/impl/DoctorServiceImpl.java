package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.AppointmentRepo;
import org.example.doctorplus.repo.DoctorRepo;
import org.example.doctorplus.service.DoctorService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;

    @Override
    public List<Doctor> getAllDoctorsSortedBy(String sortBy) {
        List<String> allowedFields = Arrays.asList("surname", "name", "speciality", "schedule");
        if (!allowedFields.contains(sortBy)) {
            sortBy = "surname";
        }
        return doctorRepo.findAll(Sort.by(Sort.Direction.ASC, sortBy));
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepo.findById(id).orElse(null);
    }

    @Override
    public void saveDoctor(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    @Override
    public void deleteAllDoctors() {
        doctorRepo.deleteAll();
    }

    @Override
    public List<Doctor> getAllDoctorsSorted(Sort sort) {
        return doctorRepo.findAll(sort);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }

    @Override
    public Doctor findByUser(User user) {
        return doctorRepo.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Доктор не найден"));
    }

    @Override
    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    @Override
    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepo.findByUserId(userId);
    }

    @Override
    public List<Doctor> getAllDoctorsWithUsers() {
        return doctorRepo.findAllWithUser();
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
}