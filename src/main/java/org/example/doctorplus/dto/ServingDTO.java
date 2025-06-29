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

    @NotBlank(message = "Название услуги обязательно")
    private String name;

    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;

    @NotNull(message = "Стоимость обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Стоимость должна быть больше нуля")
    private BigDecimal cost;

    private Long patientId; // ID пациента (необязательный)
    private Long userId;    // ID пользователя (обязательный)

    // Если вы хотите передавать данные пользователя
    private String username;
}