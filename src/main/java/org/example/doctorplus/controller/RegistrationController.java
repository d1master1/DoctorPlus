package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "include/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO dto,
                           BindingResult result,
                           RedirectAttributes redirectAttrs) {

        // Валидация паролей
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "", "Пароли не совпадают");
        }

        if (userService.existsByUsername(dto.getUsername())) {
            result.rejectValue("username", "", "Логин уже занят");
        }

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttrs.addFlashAttribute("userDTO", dto);
            return "redirect:/register";
        }

        try {
            User user = new User();
            user.setUsername(dto.getUsername());
            user.setName(dto.getName());
            user.setSurname(dto.getSurname());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setRoles(Set.of(Role.USER));

            userService.save(user);

            return "redirect:/login?success";

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }
}