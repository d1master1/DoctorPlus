package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "include/user_list";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        }

        User user = optionalUser.get();
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRoles(user.getRoles());

        model.addAttribute("userDTO", dto);
        return "include/user_form";
    }

    @PostMapping("/user/update")
    public String updateUser(@Valid @ModelAttribute("userDTO") UserDTO dto,
                             BindingResult result,
                             RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttrs.addFlashAttribute("userDTO", dto);
            return "redirect:/user/edit/" + dto.getId();
        }

        try {
            userService.update(dto);
            return "redirect:/user";
        } catch (Exception e) {
            result.reject("", "Ошибка обновления: " + e.getMessage());
            redirectAttrs.addFlashAttribute("userDTO", dto);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            return "redirect:/user/edit/" + dto.getId();
        }
    }
}