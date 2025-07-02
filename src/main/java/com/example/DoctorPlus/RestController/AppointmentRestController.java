package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    public AppointmentRestController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Получить все записи
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    // Получить запись по ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Создать новую запись
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        Appointment saved = appointmentService.saveAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Обновить существующую запись
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointmentDetails) {

        return appointmentService.findById(id)
                .map(appointment -> {
                    appointment.setDoctor(appointmentDetails.getDoctor());
                    appointment.setPatient(appointmentDetails.getPatient());
                    appointment.setServing(appointmentDetails.getServing());
                    appointment.setDate(appointmentDetails.getDate());
                    Appointment updated = appointmentService.saveAppointment(appointment);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Удалить запись по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (!appointmentService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.noContent().build();
    }

    // Удалить все записи
    @DeleteMapping
    public ResponseEntity<Void> deleteAllAppointments() {
        appointmentService.deleteAllAppointments();
        return ResponseEntity.noContent().build();
    }
}