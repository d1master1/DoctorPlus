package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.dto.UserDTO;
import com.example.DoctorPlus.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/include/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "include/registration";
    }

    @PostMapping("/include/registration/save")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            return "include/registration";
        }

        if (!userService.isUsernameAvailable(userDTO.getUsername())) {
            model.addAttribute("usernameError", "Логин уже занят");
            return "include/registration";
        }

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            model.addAttribute("passwordMismatch", "Пароли не совпадают");
            return "include/registration";
        }

        userService.save(userDTO);
        return "redirect:/include/login";
    }
}