package org.example.doctorplus.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.doctorplus.model.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 20, message = "Логин должен содержать от 3 до 20 символов")
    private String username;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть от 2 до 30 символов")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, max = 30, message = "Фамилия должна быть от 2 до 30 символов")
    private String surname;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String confirmPassword;

    @NotEmpty(message = "Выберите хотя бы одну роль")
    private Set<Role> roles = new HashSet<>();

    public boolean isPasswordConfirmed() {
        return true;
    }
}