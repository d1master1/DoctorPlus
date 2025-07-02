package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.service.ServingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servings")
public class ServingRestController {

    private final ServingService servingService;

    public ServingRestController(ServingService servingService) {
        this.servingService = servingService;
    }

    // Получить все услуги
    @GetMapping
    public ResponseEntity<List<Serving>> getAllServings() {
        List<Serving> servings = servingService.findAll();
        return ResponseEntity.ok(servings);
    }

    // Получить услугу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Serving> getServingById(@PathVariable Long id) {
        Serving serving = servingService.getServingById(id);
        return ResponseEntity.ok(serving);
    }

    // Создать новую услугу
    @PostMapping
    public ResponseEntity<Serving> createServing(@RequestBody Serving serving) {
        Serving savedServing = servingService.saveServing(serving);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServing);
    }

    // Обновить существующую услугу
    @PutMapping("/{id}")
    public ResponseEntity<Serving> updateServing(
            @PathVariable Long id,
            @RequestBody Serving servingDetails) {

        Serving serving = servingService.getServingById(id);
        serving.setName(servingDetails.getName());
        serving.setDescription(servingDetails.getDescription());
        serving.setCost(servingDetails.getCost());

        Serving updatedServing = servingService.saveServing(serving);
        return ResponseEntity.ok(updatedServing);
    }

    // Удалить услугу по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServing(@PathVariable Long id) {
        if (servingService.existsByServingId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("X-Error", "Невозможно удалить: услуга участвует в записях на прием.")
                    .build();
        }
        servingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Удалить все услуги, кроме тех, которые используются
    @DeleteMapping
    public ResponseEntity<Void> deleteAllServings() {
        List<Serving> deleted = servingService.deleteAllExceptWithAppointments();

        if (deleted.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .header("X-Warning", "Все услуги участвуют в записях на прием — ничего не удалено.")
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Deleted-Count", String.valueOf(deleted.size()))
                .build();
    }
}