package org.example.service;

import org.example.dto.DoctorDTO;
import org.example.model.Doctor;
import lombok.RequiredArgsConstructor;
import org.example.repo.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepo;

    public List<DoctorDTO> findAllSorted(String sort) {
        List<Doctor> doctors = switch (sort) {
            case "name" -> doctorRepo.findAllByOrderByNameAsc();
            case "specialty" -> doctorRepo.findAllByOrderBySpecialtyAsc();
            default -> doctorRepo.findAll();
        };
        return doctors.stream()
                .map(d -> new DoctorDTO())
                .collect(Collectors.toList());
    }

    public void create(DoctorDTO dto) {
        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setSpecialty(dto.getSpecialty());
        doctorRepo.save(doctor);
    }

    public void update(DoctorDTO dto) {
        Doctor doctor = doctorRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Врач не найден"));
        doctor.setName(dto.getName());
        doctor.setSpecialty(dto.getSpecialty());
        doctorRepo.save(doctor);
    }

    public void delete(Long id) {
        doctorRepo.deleteById(id);
    }

    public DoctorDTO findById(Long id) {
        return doctorRepo.findById(id)
                .map(d -> new DoctorDTO())
                .orElseThrow(() -> new IllegalArgumentException("Врач не найден"));
    }

    public List<DoctorDTO> findAll() {
        return doctorRepo.findAll().stream()
                .map(d -> new DoctorDTO())
                .collect(Collectors.toList());
    }
}