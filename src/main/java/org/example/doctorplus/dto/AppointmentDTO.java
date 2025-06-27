package org.example.doctorplus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.doctorplus.model.Appointment;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentDTO {

    @NotNull(message = "Дата не может быть пустой")
    private LocalDateTime date;

    @NotEmpty(message = "Статус не может быть пустым")
    private String status;

    private Long patientId;
    private Long doctorId;
    private Long servingId;

    // Конструктор из модели
    public static AppointmentDTO fromEntity(Appointment appointment) {
        if (appointment == null) return new AppointmentDTO();
        return new AppointmentDTO(
                appointment.getDate(),
                appointment.getStatus(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getServing() != null ? appointment.getServing().getId() : null
        );
    }
}