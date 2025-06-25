package org.example.doctorplus.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AppointmentDTO {
    @NotEmpty(message = "Дата не может быть пустой")
    private LocalDateTime date;

    @NotEmpty(message = "Статус не может быть пустым")
    private String status;
}