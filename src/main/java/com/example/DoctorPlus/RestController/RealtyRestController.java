/*
package com.example.DoctorPlus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.terranova.model.Realty;
import org.example.terranova.service.RealtyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realty")
@Tag(name = "Недвижимости", description = "CRUD операции для недвижимости")
public class RealtyRestController {

    private final RealtyService realtyService;

    public RealtyRestController(RealtyService realtyService) {
        this.realtyService = realtyService;
    }

    @Operation(summary = "Получить список всей недвижимости")
    @GetMapping
    public ResponseEntity<List<Realty>> getAllRealty() {
        List<Realty> realties = realtyService.findAll();
        return ResponseEntity.ok(realties);
    }

    @Operation(summary = "Получить недвижимость по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Realty> getRealtyById(@PathVariable Long id) {
        Realty realty = realtyService.getRealtyById(id);
        if (realty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(realty);
    }

    @Operation(summary = "Создать новую недвижимость")
    @PostMapping
    public ResponseEntity<Realty> createRealty(@Valid @RequestBody Realty realty) {
        Realty savedRealty = realtyService.saveRealty(realty);
        return new ResponseEntity<>(savedRealty, HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить существующую недвижимость")
    @PutMapping("/{id}")
    public ResponseEntity<Realty> updateRealty(@PathVariable Long id, @Valid @RequestBody Realty realty) {
        Realty existingRealty = realtyService.getRealtyById(id);
        if (existingRealty == null) {
            return ResponseEntity.notFound().build();
        }
        realty.setId(id);
        Realty updatedRealty = realtyService.saveRealty(realty);
        return ResponseEntity.ok(updatedRealty);
    }

    @Operation(summary = "Удалить недвижимость по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealty(@PathVariable Long id) {
        Realty existingRealty = realtyService.getRealtyById(id);
        if (existingRealty == null) {
            return ResponseEntity.notFound().build();
        }
        realtyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить всю недвижимость")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllRealty() {
        realtyService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
*/
