package org.example.doctorplus.dto;

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

    @NotEmpty(message = "Фамилия не может быть пустой")
    @Size(min = 3, max = 30, message = "От 3 до 30 символов")
    private String surname;

    // Отчество - может быть пустым
    private String patronymic;

    @NotEmpty(message = "Специальность не может быть пустой")
    private String speciality;

    @NotEmpty(message = "Расписание не может быть пустым")
    private String schedule;

    // email - может быть пустым
    private String email;

    // телефон - может быть пустым
    private String phone;
}