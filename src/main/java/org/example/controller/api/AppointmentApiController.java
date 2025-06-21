package org.example.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.dto.AppointmentDTO;
import org.example.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {
    private final AppointmentService appointmentService;

    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentDTO dto) {
        appointmentService.create(dto);
        return ResponseEntity.ok().build();
    }
}