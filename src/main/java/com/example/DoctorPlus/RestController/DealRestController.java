/*
package com.example.DoctorPlus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.terranova.model.Deal;
import org.example.terranova.service.DealService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
@Tag(name = "Сделки", description = "REST-операции для управления сделками")
public class DealRestController {

    private final DealService dealService;

    @GetMapping
    @Operation(
            summary = "Получить список сделок",
            description = "Возвращает список всех сделок с возможностью сортировки"
    )
    public ResponseEntity<List<Deal>> getAllDeals(
            @Parameter(description = "Поле сортировки", example = "amount") @RequestParam(required = false) String sortField,
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

        return ResponseEntity.ok(dealService.getAllDeals(sort));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить сделку по ID",
            description = "Возвращает сделку по заданному идентификатору"
    )
    public ResponseEntity<Deal> getDealById(
            @Parameter(description = "Идентификатор сделки", example = "1") @PathVariable Long id
    ) {
        Optional<Deal> deal = dealService.findById(id);
        return deal.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(
            summary = "Создать новую сделку",
            description = "Сохраняет новую сделку в базе данных"
    )
    public ResponseEntity<Deal> createDeal(
            @RequestBody Deal deal
    ) {
        dealService.saveDeal(deal);
        return ResponseEntity.ok(deal);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновить сделку",
            description = "Обновляет существующую сделку по ID"
    )
    public ResponseEntity<Deal> updateDeal(
            @Parameter(description = "ID сделки для обновления", example = "1") @PathVariable Long id,
            @RequestBody Deal deal
    ) {
        if (!dealService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        deal.setId(id);
        dealService.saveDeal(deal);
        return ResponseEntity.ok(deal);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить сделку",
            description = "Удаляет сделку по заданному ID"
    )
    public ResponseEntity<Void> deleteDeal(
            @Parameter(description = "ID сделки для удаления", example = "1") @PathVariable Long id
    ) {
        dealService.deleteDeal(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "Удалить все сделки",
            description = "Удаляет все сделки из базы данных"
    )
    public ResponseEntity<Void> deleteAllDeals() {
        dealService.deleteAllDeals();
        return ResponseEntity.noContent().build();
    }
}
*/
