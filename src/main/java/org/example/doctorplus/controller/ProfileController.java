package org.example.doctorplus.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.ServingService;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    @Getter
    private final PatientService patientService;
    private final ServingService servingService;
    private final AppointmentService appointmentService;

    // Отображение профиля
    @GetMapping("/profile")
    public String viewProfile(Principal principal, Model model) {
        String username = principal.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        List<Appointment> appointments = appointmentService.findAllByUser(user);
        List<Serving> servings = servingService.findAllByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("appointments", appointments);
        model.addAttribute("servings", servings);

        return "include/profile";
    }
}