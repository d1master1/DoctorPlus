/*
package com.example.DoctorPlus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.terranova.dto.EmployeeDTO;
import org.example.terranova.model.Employee;
import org.example.terranova.service.EmployeeService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Сотрудники", description = "REST API для управления сотрудниками")
public class EmployeeRestController {

    private final EmployeeService employeeService;

    @GetMapping
    @Operation(
            summary = "Получить список всех сотрудников",
            description = "Возвращает список всех сотрудников с возможностью сортировки"
    )
    public ResponseEntity<List<Employee>> getAllEmployees(
            @Parameter(description = "Поле сортировки", example = "surname") @RequestParam(required = false) String sortBy,
            @Parameter(description = "Направление сортировки (asc/desc)", example = "asc") @RequestParam(required = false) String sortDir
    ) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        return ResponseEntity.ok(employeeService.getAllEmployeesSorted(sort));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить сотрудника по ID",
            description = "Возвращает сотрудника по заданному ID"
    )
    public ResponseEntity<Employee> getEmployeeById(
            @Parameter(description = "ID сотрудника", example = "1") @PathVariable Long id
    ) {
        Optional<Employee> employee = Optional.ofNullable(employeeService.getEmployeeById(id));
        return employee.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Создать нового сотрудника",
            description = "Создаёт нового сотрудника по переданным данным"
    )
    public ResponseEntity<Employee> createEmployee(
            @Valid @RequestBody EmployeeDTO dto
    ) {
        Employee employee = new Employee();
        copyDtoToEntity(dto, employee);
        employeeService.saveEmployee(employee);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить данные сотрудника",
            description = "Обновляет существующего сотрудника по ID"
    )
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto
    ) {
        Employee existing = employeeService.getEmployeeById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        copyDtoToEntity(dto, existing);
        employeeService.saveEmployee(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить сотрудника",
            description = "Удаляет сотрудника по ID"
    )
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id
    ) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Удалить всех сотрудников",
            description = "Удаляет всех сотрудников из базы данных"
    )
    public ResponseEntity<Void> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return ResponseEntity.noContent().build();
    }

    // Вспомогательный метод
    private void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPatronymic(dto.getPatronymic());
        entity.setPosition(dto.getPosition());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
    }
}
*/
