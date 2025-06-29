package org.example.doctorplus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.impl.AppointmentServiceImpl;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private AppointmentServiceImpl patientService;

    // --- Отображение списка пользователей --- //
    @GetMapping
    public String listUsers(Model model,
                            @RequestParam(name = "sort", required = false) String sort) {
        List<User> users = switch (sort != null ? sort.toLowerCase() : "") {
            case "username_asc" -> userService.findAllOrderByUsernameAsc();
            case "username_desc" -> userService.findAllOrderByUsernameDesc();
            case "name_asc" -> userService.findAllOrderByNameAsc();
            case "name_desc" -> userService.findAllOrderByNameDesc();
            default -> userService.findAll();
        };

        model.addAttribute("users", users);
        model.addAttribute("sort", sort);

        return "user/user_list";
    }

    // --- Форма создания пользователя --- //
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        model.addAttribute("allRoles", Role.values());
        return "user/user_form";
    }

    @GetMapping("/patient/add")
    public String showAddForm(Model model) throws JsonProcessingException {
        model.addAttribute("patient", new Patient());
        model.addAttribute("users", userService.findAll());

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("userJson", mapper.writeValueAsString(userService.findAll()));
        return "patient/patient_form";
    }

    @GetMapping("/patient/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) throws JsonProcessingException {
        Optional<Appointment> optionalPatient = patientService.findById(id);
        if (optionalPatient.isEmpty()) {
            return "redirect:/patient?error=true";
        }

        Appointment patient = optionalPatient.get();

        model.addAttribute("patient", patient);
        model.addAttribute("users", userService.findAll());

        ObjectMapper mapper = new ObjectMapper();
        model.addAttribute("userJson", mapper.writeValueAsString(userService.findAll()));

        return "patient/patient_form";
    }

    // --- Сохранение или обновление пользователя --- //
    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("userDTO") UserDTO dto,
                           BindingResult result,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", Role.values());
            return "user/user_form";
        }

        userService.save(dto);
        return "redirect:/user";
    }

    // --- Удаление пользователя --- //
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }

    @PostMapping("/update")
    public String updateUser(
            @Valid @ModelAttribute("user") UserDTO userDTO,
            @RequestParam(value = "roles", required = false) List<String> roleNames,
            BindingResult result,
            RedirectAttributes redirectAttrs) {

        // 1. Проверяем валидацию основных полей
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttrs.addFlashAttribute("user", userDTO);
            return "redirect:/user/edit/" + userDTO.getUsername();
        }

        // 2. Преобразуем строки в Enum Role
        Set<Role> roles = new HashSet<>();
        if (roleNames != null && !roleNames.isEmpty()) {
            for (String roleName : roleNames) {
                try {
                    roles.add(Role.valueOf(roleName));
                } catch (IllegalArgumentException ex) {
                    result.rejectValue("roles", "", "Недопустимая роль: " + roleName);
                }
            }
        }

        try {
            // 4. Обновляем пользователя
            userService.update(userDTO);
            redirectAttrs.addFlashAttribute("successMessage", "Данные пользователя успешно обновлены");
        } catch (Exception e) {
            result.reject("", "Ошибка при обновлении пользователя: " + e.getMessage());
            redirectAttrs.addFlashAttribute("user", userDTO);
            redirectAttrs.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            return "redirect:/user/edit/" + userDTO.getUsername();
        }

        return "redirect:/user";
    }
}