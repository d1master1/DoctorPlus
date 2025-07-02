package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;

    public DoctorRestController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Получить всех врачей
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctorsWithUsers();
        return ResponseEntity.ok(doctors);
    }

    // Получить врача по ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorByIdOptional(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Создать нового врача
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorService.saveDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }

    // Обновить существующего врача
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctorDetails) {

        return doctorService.getDoctorByIdOptional(id)
                .map(doctor -> {
                    doctor.setName(doctorDetails.getName());
                    doctor.setSurname(doctorDetails.getSurname());
                    doctor.setPatronymic(doctorDetails.getPatronymic());
                    doctor.setUser(doctorDetails.getUser());
                    Doctor updated = doctorService.saveDoctor(doctor);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Удалить врача по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Удалить всех врачей
    @DeleteMapping
    public ResponseEntity<Void> deleteAllDoctors() {
        List<Doctor> notDeleted = doctorService.deleteAllExceptWithAppointments();
        if (notDeleted.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("X-Pending-Deletion", String.valueOf(notDeleted.size()))
                    .build();
        }
    }

    // Получить отчество по userId
    @GetMapping("/patronymic/{userId}")
    public ResponseEntity<Map<String, String>> getPatronymicByUserId(@PathVariable Long userId) {
        Doctor doctor = doctorService.getDoctorByUserId(userId);
        if (doctor != null && doctor.getPatronymic() != null) {
            return ResponseEntity.ok(Map.of("patronymic", doctor.getPatronymic()));
        } else {
            return ResponseEntity.ok(Map.of("patronymic", ""));
        }
    }
}