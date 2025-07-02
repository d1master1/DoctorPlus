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

    /*@NotEmpty(message = "Номер телефона не может быть пустым")
    @Size(min = 3, max = 10, message = "От 3 до 10 символов")
    private String number;
    @NotEmpty(message = "Цена не может быть пустым")
    private double amount;*/
}