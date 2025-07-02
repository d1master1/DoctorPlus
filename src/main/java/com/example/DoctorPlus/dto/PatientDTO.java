package com.example.DoctorPlus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class PatientDTO {
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 3, max = 30, message = "От 3 до 20 символов")
    private String name;
    @NotEmpty(message = "Фамилия не может быть пустым")
    @Size(min = 3, max = 30, message = "От 3 до 30 символов")
    private String surname;
    // Отчество - может быть пустым
    private String patronymic;

    @NotEmpty(message = "Номер телефона не может быть пустым")
    @Size(min = 3, max = 10, message = "От 3 до 10 символов")
    private String phone;
    // company - может быть пустым
    private String email;
    // license - может быть пустым
    private String address;

    /*// ownerType - может быть пустым
    private String ownerType;
    @NotEmpty(message = "Паспорт не может быть пустым")
    private String passport;*/
}