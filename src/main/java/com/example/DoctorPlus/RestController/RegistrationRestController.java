package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.dto.UserDTO;
import com.example.DoctorPlus.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class RegistrationRestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        if (!userService.isUsernameAvailable(userDTO.getUsername())) {
            response.put("usernameError", "Логин уже занят");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            response.put("passwordMismatch", "Пароли не совпадают");
            return ResponseEntity.badRequest().body(response);
        }

        userService.save(userDTO);
        response.put("message", "Регистрация успешна");
        return ResponseEntity.ok(response);
    }
}