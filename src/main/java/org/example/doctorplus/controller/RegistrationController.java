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

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "include/login";
    }

    @PostMapping("/include/registration/save")
    public String registerUser(
            @Valid @ModelAttribute("user") UserDTO userDTO,
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
        return "redirect:/login";
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "include/registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            return "include/registration";
        }

        if (!userDTO.isPasswordConfirmed()) {
            model.addAttribute("passwordMismatch", "Пароли не совпадают");
            return "include/registration";
        }

        userService.save(userDTO); // сохраняет только username, name, surname, password и роли
        return "redirect:/login?success";
    }
}