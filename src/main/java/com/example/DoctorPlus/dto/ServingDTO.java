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

    /*@NotEmpty(message = "Улица не может быть пустым")
    @Size(min = 3, max = 50, message = "От 3 до 20 символов")
    private String street;
    @NotEmpty(message = "Количество комнат не может быть пустым")
    private int rooms;
    @NotEmpty(message = "floors комнат не может быть пустым")
    private int floors;
    @NotEmpty(message = "square комнат не может быть пустым")
    private double square;
    @NotEmpty(message = "repair комнат не может быть пустым")
    private String repair;
    @NotEmpty(message = "elevator комнат не может быть пустым")
    private boolean elevator;
    @NotEmpty(message = "playgraund комнат не может быть пустым")
    private boolean playground;
    @NotEmpty(message = "trashChute комнат не может быть пустым")
    private boolean trashChute;
    @NotEmpty(message = "parking комнат не может быть пустым")
    private boolean parking;
    @NotEmpty(message = "balcony комнат не может быть пустым")
    private boolean balcony;*/
}