package org.example.doctorplus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServingDTO {
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 3, max = 50, message = "От 3 до 50 символов")
    private String name;

    @NotEmpty(message = "Описание не может быть пустым")
    private String description;

    @NotEmpty(message = "Стоимость не может быть пустой")
    private double cost;
}