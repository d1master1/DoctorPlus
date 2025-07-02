package com.example.DoctorPlus.dto;

import com.example.DoctorPlus.model.Role;
import com.example.DoctorPlus.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank(message = "Логин не может быть пустым")
    @Size(min = 3, max = 20, message = "От 3 до 20 символов")
    private String username;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "От 8 до 20 символов")
    private String password;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "От 8 до 20 символов")
    private String confirmPassword;

    private Set<Role> roles = new HashSet<>();

    // Статический метод преобразования из сущности User в DTO
    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        // Пароль и confirmPassword, как правило, не копируются из сущности (безопасность)
        dto.setRoles(user.getRoles() != null ? new HashSet<>(user.getRoles()) : new HashSet<>());
        return dto;
    }
}