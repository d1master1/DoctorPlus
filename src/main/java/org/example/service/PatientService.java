package org.example.service;

import org.example.dto.PatientDTO;
import org.example.model.Patient;
import lombok.RequiredArgsConstructor;
import org.example.repo.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepo;

    public List<PatientDTO> findAll() {
        return patientRepo.findAll().stream()
                .map(p -> new PatientDTO())
                .collect(Collectors.toList());
    }

    public void create(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patientRepo.save(patient);
    }

    public List<PatientDTO> findAllSorted(String sort) {
        List<Patient> patients = switch (sort) {
            case "name" -> patientRepo.findAllByOrderByNameAsc();
            case "email" -> patientRepo.findAllByOrderByEmailAsc();
            default -> patientRepo.findAll();
        };
        return patients.stream()
                .map(p -> new PatientDTO())
                .collect(Collectors.toList());
    }

    public PatientDTO findById(Long id) {
        return patientRepo.findById(id)
                .map(p -> new PatientDTO())
                .orElseThrow(() -> new IllegalArgumentException("Пациент не найден"));
    }

    public void update(PatientDTO dto) {
        Patient patient = patientRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пациент не найден"));

        patient.setName(dto.getName());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());

        patientRepo.save(patient);
    }

    public void delete(Long id) {
        if (!patientRepo.existsById(id)) {
            throw new IllegalArgumentException("Пациент не найден");
        }
        patientRepo.deleteById(id);
    }
}