package org.example.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.LoginDTO;
import org.example.dto.RegistrationDTO;
import org.example.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", RegistrationDTO.builder().build());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegistrationDTO dto,
                               BindingResult result,
                               Model model) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Пароли не совпадают");
            return "auth/register";
        }

        try {
            userService.register(dto);
        } catch (RuntimeException ex) {
            result.rejectValue("username", null, "Логин уже используется");
            return "auth/register";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginData", new LoginDTO());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/home";
    }
}