package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.dto.UserDTO;
import com.example.DoctorPlus.model.Role;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    // Получить всех пользователей
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    // Получить пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(convertToDTO(user));
    }

    // Создать нового пользователя
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User savedUser = userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
    }

    // Обновить пользователя
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        User updatedUser = userService.update(userDTO);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    // Удалить пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Сортировка пользователей
    @GetMapping("/sorted")
    public ResponseEntity<List<UserDTO>> getUsersSorted(@RequestParam(name = "sort", required = false) String sort) {
        List<User> users = switch (sort != null ? sort.toLowerCase() : "") {
            case "username_asc" -> userService.findAllOrderByUsernameAsc();
            case "username_desc" -> userService.findAllOrderByUsernameDesc();
            case "name_asc" -> userService.findAllOrderByNameAsc();
            case "name_desc" -> userService.findAllOrderByNameDesc();
            default -> userService.findAll();
        };

        List<UserDTO> userDTOs = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    // Получить все доступные роли
    @GetMapping("/roles")
    public ResponseEntity<Set<String>> getAllRoles() {
        Set<String> roleNames = Set.of(Role.values())
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(roleNames);
    }

    // Приватный метод: конвертация User → UserDTO
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getRoles()
                        .stream()
                        .map(r -> r.name().toUpperCase())
                        .collect(Collectors.toSet())
        );
    }
}