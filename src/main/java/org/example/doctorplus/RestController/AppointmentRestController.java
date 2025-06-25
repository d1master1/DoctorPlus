package org.example.doctorplus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.service.AppointmentService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Приемы", description = "REST-операции для управления приемами")
public class AppointmentRestController {
    private final AppointmentService appointmentService;

    @GetMapping
    @Operation(summary = "Получить список приемов", description = "Возвращает список всех приемов с возможностью сортировки")
    public ResponseEntity<List<Appointment>> getAllAppointments(
            @Parameter(description = "Поле сортировки", example = "date") @RequestParam(required = false) String sortField,
            @Parameter(description = "Направление сортировки", example = "asc") @RequestParam(required = false) String sortDir
    ) {
        Sort sort;
        if (sortField == null || sortField.isEmpty()) {
            sort = Sort.unsorted();
        } else {
            try {
                sort = Sort.by(Sort.Direction.fromString(sortDir), sortField);
            } catch (IllegalArgumentException ex) {
                sort = Sort.by(Sort.Direction.ASC, "date");
            }
        }
        return ResponseEntity.ok(appointmentService.getAllAppointments(sort));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить прием по ID", description = "Возвращает прием по заданному идентификатору")
    public ResponseEntity<Appointment> getAppointmentById(
            @Parameter(description = "Идентификатор приема", example = "1") @PathVariable Long id
    ) {
        Optional<Appointment> appointment = appointmentService.findById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новый прием", description = "Сохраняет новый прием в базе данных")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить прием", description = "Обновляет существующий прием по ID")
    public ResponseEntity<Appointment> updateAppointment(
            @Parameter(description = "ID приема для обновления", example = "1") @PathVariable Long id,
            @RequestBody Appointment appointment
    ) {
        if (!appointmentService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        appointment.setId(id);
        appointmentService.saveAppointment(appointment);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить прием", description = "Удаляет прием по заданному ID")
    public ResponseEntity<Void> deleteAppointment(
            @Parameter(description = "ID приема для удаления", example = "1") @PathVariable Long id
    ) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Удалить все приемы", description = "Удаляет все приемы из базы данных")
    public ResponseEntity<Void> deleteAllAppointments() {
        appointmentService.deleteAllAppointments();
        return ResponseEntity.noContent().build();
    }
}