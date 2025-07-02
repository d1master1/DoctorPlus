package com.example.DoctorPlus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class AppointmentDTO {
    @NotEmpty(message = "Дата не может быть пустым")
    private LocalDate date;
    @NotEmpty(message = "Статус сделки не может быть пустым")
    private String status;
}