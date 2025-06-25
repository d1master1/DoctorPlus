package org.example.doctorplus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Пациенты", description = "Операции с пациентами")
public class PatientRestController {
    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Получить всех пациентов", description = "Возвращает список всех пациентов в системе")
    @ApiResponse(responseCode = "200", description = "Успешно получен список пациентов")
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @Operation(summary = "Получить пациента по ID", description = "Возвращает пациента по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Пациент найден")
    @ApiResponse(responseCode = "404", description = "Пациент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @Parameter(description = "ID пациента", example = "1")
            @PathVariable Long id
    ) {
        return patientService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать нового пациента")
    @PostMapping
    public ResponseEntity<Patient> createPatient(
            @RequestBody Patient patient
    ) {
        patientService.save(patient);
        return ResponseEntity.ok(patient);
    }

    @Operation(summary = "Обновить данные пациента")
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient
    ) {
        if (patientService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        patient.setId(id);
        patientService.save(patient);
        return ResponseEntity.ok(patient);
    }

    @Operation(summary = "Удалить пациента по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID пациента", example = "1")
            @PathVariable Long id
    ) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить всех пациентов")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPatients() {
        patientService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}