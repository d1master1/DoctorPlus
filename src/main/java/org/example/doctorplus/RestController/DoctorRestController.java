package org.example.doctorplus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.DoctorDTO;
import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.service.DoctorService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Докторы", description = "REST API для управления докторами")
public class DoctorRestController {
    private final DoctorService doctorService;

    @GetMapping
    @Operation(
            summary = "Получить список всех докторов",
            description = "Возвращает список всех докторов с возможностью сортировки"
    )
    public ResponseEntity<List<Doctor>> getAllDoctors(
            @Parameter(description = "Поле сортировки", example = "surname") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Направление сортировки (asc/desc)", example = "asc") @RequestParam(required = false) String sortDir
    ) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        return ResponseEntity.ok(doctorService.getAllDoctorsSorted(sort));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить доктора по ID",
            description = "Возвращает доктора по заданному ID"
    )
    public ResponseEntity<Doctor> getDoctorById(
            @Parameter(description = "ID доктора", example = "1") @PathVariable Long id
    ) {
        Optional<Doctor> doctor = Optional.ofNullable(doctorService.getDoctorById(id));
        return doctor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Создать нового доктора",
            description = "Создаёт нового доктора по переданным данным"
    )
    public ResponseEntity<Doctor> createDoctor(
            @Valid @RequestBody DoctorDTO dto
    ) {
        Doctor doctor = new Doctor();
        copyDtoToEntity(dto, doctor);
        doctorService.saveDoctor(doctor);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить данные доктора",
            description = "Обновляет существующего доктора по ID"
    )
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorDTO dto
    ) {
        Doctor existing = doctorService.getDoctorById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        copyDtoToEntity(dto, existing);
        doctorService.saveDoctor(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить доктора",
            description = "Удаляет доктора по ID"
    )
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id
    ) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Удалить всех докторов",
            description = "Удаляет всех докторов из базы данных"
    )
    public ResponseEntity<Void> deleteAllDoctors() {
        doctorService.deleteAllDoctors();
        return ResponseEntity.noContent().build();
    }

    // Вспомогательный метод
    private void copyDtoToEntity(DoctorDTO dto, Doctor entity) {
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPatronymic(dto.getPatronymic());
        entity.setSpeciality(dto.getSpeciality());
        entity.setSchedule(dto.getSchedule());
    }
}