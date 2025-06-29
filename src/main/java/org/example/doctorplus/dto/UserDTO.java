package org.example.doctorplus.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов")
    private String username;

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Фамилия обязательна")
    private String surname;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    private String confirmPassword;

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}