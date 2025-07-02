package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {

    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Получить всех пациентов
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    // Получить пациента по ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patientOpt = patientService.findById(id);
        return patientOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Создать нового пациента
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPatient);
    }

    // Обновить существующего пациента
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patientDetails) {

        Optional<Patient> patientOpt = patientService.findById(id);

        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Patient patient = patientOpt.get();
        patient.setName(patientDetails.getName());
        patient.setSurname(patientDetails.getSurname());
        patient.setPhone(patientDetails.getPhone());
        patient.setEmail(patientDetails.getEmail());
        patient.setBirth(patientDetails.getBirth());

        Patient updatedPatient = patientService.save(patient);
        return ResponseEntity.ok(updatedPatient);
    }

    // Удалить пациента по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        boolean deleted = patientService.deleteByIdIfPossible(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // есть связанные записи
        }
        return ResponseEntity.noContent().build();
    }

    // Удалить всех пациентов
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPatients() {
        int notDeletedCount = patientService.deleteAllExceptWithAppointments();

        if (notDeletedCount > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("X-Not-Deleted", String.valueOf(notDeletedCount))
                    .build();
        }

        return ResponseEntity.noContent().build();
    }
}