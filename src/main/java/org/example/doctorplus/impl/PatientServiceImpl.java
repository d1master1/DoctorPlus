package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.repo.PatientRepo;
import org.example.doctorplus.service.PatientService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;

    @Override
    public List<Patient> findAll() {
        return patientRepo.findAll();
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepo.findById(id);
    }

    @Override
    public void save(Patient patient) {
        patientRepo.save(patient);
    }

    @Override
    public boolean deleteByIdIfPossible(Long id) {
        if (existsById(id)) {
            patientRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Patient> findAllSortedByField(String field, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(Sort.Direction.DESC, field)
                : Sort.by(Sort.Direction.ASC, field);

        return patientRepo.findAll(sort);
    }

    @Override
    public int deleteAllExceptWithAppointments() {
        List<Patient> allPatients = findAll();
        for (Patient patient : allPatients) {
            if (hasAppointments()) continue;
            patientRepo.deleteById(patient.getId());
        }
        return allPatients.size(); // можно вернуть количество удалённых
    }

    private boolean hasAppointments() {
        // реализуй проверку, если нужно
        return false; // пока заглушка
    }

    private boolean existsById(Long id) {
        return patientRepo.existsById(id);
    }
}