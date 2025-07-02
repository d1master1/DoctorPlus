package com.example.DoctorPlus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorDTO {
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 3, max = 30, message = "От 3 до 30 символов")
    private String name;
    @NotEmpty(message = "Фамилия не может быть пустым")
    @Size(min = 3, max = 30, message = "От 3 до 30 символов")
    private String surname;
    // Отчество - может быть пустым
    private String patronymic;

    @NotEmpty(message = "Должность не может быть пустым")
    private String speciality;
    @NotEmpty(message = "Специализация не может быть пустым")
    private String schedule;

    /*@NotEmpty(message = "Номер телефона не может быть пустым")
    @Size(min = 3, max = 10, message = "От 3 до 10 символов")
    private String phone;*/
}