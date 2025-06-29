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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "include/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO dto,
                           BindingResult result,
                           RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttrs.addFlashAttribute("userDTO", dto);
            return "redirect:/register";
        }

        try {
            userService.save(dto);
            return "redirect:/login?success";
        } catch (Exception e) {
            result.reject("", "Ошибка регистрации: " + e.getMessage());
            redirectAttrs.addFlashAttribute("userDTO", dto);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            return "redirect:/register";
        }
    }
}