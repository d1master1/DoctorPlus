package org.example.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {
    private Long id;
    private LocalDateTime dateTime;
    private Long patientId;
    private Long doctorId;
    private Long serviceId;
}