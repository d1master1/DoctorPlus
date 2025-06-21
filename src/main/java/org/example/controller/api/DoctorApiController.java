package org.example.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.dto.DoctorDTO;
import org.example.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorApiController {
    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createDoctor(@RequestBody DoctorDTO dto) {
        doctorService.create(dto);
        return ResponseEntity.ok().build();
    }
}