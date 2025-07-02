package com.example.DoctorPlus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServingDTO {
    @NotEmpty(message = "Регион не может быть пустым")
    @Size(min = 3, max = 50, message = "От 3 до 20 символов")
    private String name;
    @NotEmpty(message = "Местонахождение не может быть пустым")
    @Size(min = 3, max = 50, message = "От 3 до 20 символов")
    private String description;
    @NotEmpty(message = "cost комнат не может быть пустым")
    private double cost;
}