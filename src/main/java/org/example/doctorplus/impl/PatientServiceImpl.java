package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.AppointmentRepo;
import org.example.doctorplus.repo.PatientRepo;
import org.example.doctorplus.service.PatientService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepo patientRepository;
    private final AppointmentRepo appointmentRepository;

    private static final List<String> ALLOWED_FIELDS = List.of("name", "surname", "passport", "phone");

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> findAllSortedByField(String fieldName, String sortDirection) {
        if (fieldName == null || !ALLOWED_FIELDS.contains(fieldName.toLowerCase())) {
            throw new IllegalArgumentException("Недопустимое поле для сортировки: " + fieldName);
        }
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Недопустимое направление сортировки: " + sortDirection, e);
        }
        String actualFieldName = switch (fieldName.toLowerCase()) {
            case "name" -> "name";
            case "surname" -> "surname";
            case "passport" -> "passport";
            case "phone" -> "phone";
            default -> fieldName;
        };
        return patientRepository.findAll(Sort.by(direction, actualFieldName));
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        patientRepository.deleteAll();
    }

    @Override
    public boolean deleteByIdIfPossible(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            if (appointmentRepository.existsByPatient(patient)) {
                patientRepository.delete(patient);
                return true;
            }
        }
        return false;
    }

    @Override
    public int deleteAllExceptWithAppointments() {
        List<Patient> allPatients = patientRepository.findAll();
        int notDeletedCount = 0;
        for (Patient patient : allPatients) {
            if (appointmentRepository.existsByPatient(patient)) {
                patientRepository.delete(patient);
            } else {
                notDeletedCount++;
            }
        }
        return notDeletedCount;
    }
    @Override
    public Optional<Patient> findByUser(User user) { // Возвращает Optional<Patient>
        return patientRepository.findByUser(user);
    }
}