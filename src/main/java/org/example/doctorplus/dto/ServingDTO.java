package org.example.doctorplus.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServingDTO {

    private Long id;

    @NotBlank(message = "Название обязательно")
    private String name;

    private String description;

    @NotNull(message = "Стоимость обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Стоимость должна быть больше нуля")
    private BigDecimal cost;

    private Long userId;
    private String username;
    private Long patientId;
}