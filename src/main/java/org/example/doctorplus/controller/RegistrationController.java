package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    // Форма регистрации
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "include/registration";
    }

    // Обработка формы регистрации
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO dto,
                           BindingResult result,
                           Model model) {
        if (result.hasErrors()) {
            return "include/registration";
        }

        if (!userService.isUsernameAvailable(dto.getUsername())) {
            model.addAttribute("usernameError", "Логин уже занят");
            return "include/registration";
        }

        if (!dto.isPasswordConfirmed()) {
            model.addAttribute("passwordMismatch", "Пароли не совпадают");
            return "include/registration";
        }

        userService.save(dto);
        return "redirect:/login?success";
    }

    // Форма входа
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "include/login";
    }
}