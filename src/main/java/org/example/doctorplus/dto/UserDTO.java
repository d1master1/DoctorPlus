package org.example.doctorplus.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;

import java.time.LocalDate;
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
    private String name;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String password;

    @NotBlank(message = "Подтвердите пароль")
    @Size(min = 8, max = 20, message = "Пароль должен быть от 8 до 20 символов")
    private String confirmPassword;

    // Эти поля не обязательны при регистрации
    private String email;
    private String phone;
    private String passport;
    private LocalDate birthDate;
    private String address;

    private Set<Role> roles = new HashSet<>();

    public static UserDTO fromEntity(User user) {
        return fromEntity(user, null);
    }

    public static UserDTO fromEntity(User user, org.example.doctorplus.model.Patient patient) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setPassword(user.getPassword());
        dto.setRoles(user.getRoles());

        if (patient != null) {
            dto.setPassport(patient.getPassport());
            dto.setAddress(patient.getAddress());
            dto.setBirthDate(patient.getBirthDate());
        }

        return dto;
    }

    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}