package org.example.doctorplus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.service.ServingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servings")
@Tag(name = "Услуги", description = "CRUD операции для услуг")
public class ServingRestController {

    private final ServingService servingService;

    public ServingRestController(ServingService servingService) {
        this.servingService = servingService;
    }

    @Operation(summary = "Получить список всех услуг")
    @GetMapping
    public ResponseEntity<List<Serving>> getAllServings() {
        List<Serving> servings = servingService.findAll();
        return ResponseEntity.ok(servings);
    }

    @Operation(summary = "Получить услугу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Serving> getServingById(@PathVariable Long id) {
        Serving serving = servingService.getServingById(id);
        if (serving == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serving);
    }

    @Operation(summary = "Создать новую услугу")
    @PostMapping
    public ResponseEntity<Serving> createServing(@RequestBody Serving serving) {
        Serving saved = servingService.saveServing(serving);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Обновить существующую услугу")
    @PutMapping("/{id}")
    public ResponseEntity<Serving> updateServing(@PathVariable Long id, @RequestBody Serving serving) {
        Serving existing = servingService.getServingById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        serving.setId(id);
        return ResponseEntity.ok(servingService.saveServing(serving));
    }

    @Operation(summary = "Удалить услугу по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServing(@PathVariable Long id) {
        if (servingService.getServingById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        servingService.deleteServing(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить все услуги")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllServings() {
        servingService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}