package org.example.doctorplus.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private Long id;

    @NotBlank(message = "Фамилия обязательна")
    private String surname;

    @NotBlank(message = "Имя обязательно")
    private String name;

    private String patronymic;

    @NotBlank(message = "Паспорт обязателен")
    @Pattern(regexp = "\\d{10}", message = "Паспорт должен содержать 10 цифр")
    private String passport;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный телефон")
    private String phone;

    @Email(message = "Некорректный email")
    private String email;

    private String address;

    // Добавлено поле birthDate
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthDate;
}