/*
package com.example.DoctorPlus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.terranova.model.Client;
import org.example.terranova.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Клиенты", description = "Операции с клиентами")
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Получить всех клиентов", description = "Возвращает список всех клиентов в системе")
    @ApiResponse(responseCode = "200", description = "Успешно получен список клиентов")
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @Operation(summary = "Получить клиента по ID", description = "Возвращает клиента по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Клиент найден")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(
            @Parameter(description = "ID клиента", example = "1")
            @PathVariable Long id
    ) {
        return clientService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать нового клиента")
    @PostMapping
    public ResponseEntity<Client> createClient(
            @RequestBody Client client
    ) {
        return (ResponseEntity<Client>) ResponseEntity.ok();
    }

    @Operation(summary = "Удалить клиента по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID клиента", example = "1")
            @PathVariable Long id
    ) {
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
*/
