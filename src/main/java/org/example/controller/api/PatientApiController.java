package org.example.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.dto.PatientDTO;
import org.example.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientApiController {
    private final PatientService patientService;

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createPatient(@RequestBody PatientDTO dto) {
        patientService.create(dto);
        return ResponseEntity.ok().build();
    }
}