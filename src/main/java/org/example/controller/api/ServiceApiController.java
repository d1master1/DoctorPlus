package org.example.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.dto.ServiceDTO;
import org.example.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceApiController {
    private final ServiceService serviceService;

    @GetMapping
    public List<ServiceDTO> getAllServices() {
        return serviceService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createService(@RequestBody ServiceDTO dto) {
        serviceService.create(dto);
        return ResponseEntity.ok().build();
    }
}