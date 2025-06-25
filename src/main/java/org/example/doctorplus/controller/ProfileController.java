package org.example.doctorplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.example.doctorplus.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ServingService servingService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String viewProfile(Principal principal, Model model) {
        String username = principal.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        Patient patient = patientService.findByUser(user)
                .orElseThrow(() -> new UsernameNotFoundException("Пациент не найден"));

        List<Appointment> appointments = appointmentService.findByPatient(patient);
        List<Serving> servings = servingService.findByPatient(patient);

        String avatarUrl = "/img/default-avatar.png";
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            avatarUrl = user.getAvatarUrl();
        }

        model.addAttribute("user", user);
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);
        model.addAttribute("servings", servings);
        model.addAttribute("avatarUrl", avatarUrl);

        return "include/profile";
    }

    @PostMapping("/avatar")
    public String handleAvatarUpload(
            @RequestParam("avatar") MultipartFile avatar,
            Principal principal) throws IOException {

        if (avatar.isEmpty()) {
            return "redirect:/profile";
        }

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String filename = System.currentTimeMillis() + "_" + principal.getName() + ".jpg";
        Path filePath = uploadDir.resolve(filename);

        Files.write(filePath, avatar.getBytes());

        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAvatarUrl("/" + filename);
            userService.save(user);
        } else {
            throw new IOException("Пользователь не найден: " + principal.getName());
        }
        return "redirect:/profile";
    }

    @PostMapping("/edit")
    public String updateProfile(@Valid @ModelAttribute("patient") Patient patient,
                                BindingResult result,
                                Principal principal) {
        if (result.hasErrors()) {
            return "include/profile";
        }

        // Обновляем данные пациента
        patientService.save(patient);

        return "redirect:/profile";
    }
}