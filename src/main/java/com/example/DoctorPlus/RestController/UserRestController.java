/*
package com.example.DoctorPlus.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.terranova.dto.UserDTO;
import org.example.terranova.model.Role;
import org.example.terranova.model.User;
import org.example.terranova.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserRestController {

    private final UserService userService;

    @Operation(summary = "Получить список всех пользователей",
            description = "Возвращает список пользователей с возможностью сортировки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный ответ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class)))
            })
    @GetMapping
    public List<UserDTO> getAllUsers(
            @Parameter(description = "Параметр сортировки: username_asc, username_desc, name_asc, name_desc")
            @RequestParam(value = "sort", required = false) String sort) {

        List<User> users = switch (sort != null ? sort.toLowerCase() : "") {
            case "username_asc" -> userService.findAllOrderByUsernameAsc();
            case "username_desc" -> userService.findAllOrderByUsernameDesc();
            case "name_asc" -> userService.findAllOrderByNameAsc();
            case "name_desc" -> userService.findAllOrderByNameDesc();
            default -> userService.findAll();
        };

        return users.stream().map(UserDTO::fromEntity).toList();
    }

    @Operation(summary = "Создать нового пользователя",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь создан"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
            })
    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(201).body("Пользователь успешно создан");
    }

    @Operation(summary = "Обновить роли пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Роли пользователя обновлены"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @PutMapping("/{id}/roles")
    public ResponseEntity<String> updateUserRoles(
            @PathVariable Long id,
            @RequestBody Set<String> roleNames) {

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            try {
                roles.add(Role.valueOf(roleName));
            } catch (IllegalArgumentException ignored) {
                // Можно логировать ошибку
            }
        }
        userService.updateUserRoles(id, roles);
        return ResponseEntity.ok("Роли пользователя обновлены");
    }

    @Operation(summary = "Удалить пользователя по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пользователь удалён"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить пользователя по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователь найден",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }
}
*/
