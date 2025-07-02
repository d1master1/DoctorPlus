package com.example.DoctorPlus.controller;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.ServingService;
import com.example.DoctorPlus.service.UserService;
import jakarta.servlet.ServletContext;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ProfileController {

    @Autowired
    private Environment env;

    @Autowired
    private final UserService userService;
    private final ServingService servingService;
    private final AppointmentService appointmentService;
    @Autowired
    private ServletContext servletContext;

    // Значение можно получить из application.properties (fallback — "uploads")
    @Setter
    @Value("${upload.path:uploads}")
    private String uploadPath;

    public ProfileController(UserService userService, ServingService servingService, AppointmentService appointmentService) {
        this.userService = userService;
        this.servingService = servingService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/profile")
    public String userProfile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        /*List<Serving> userServings = servingService.findAllByOwner(user);*/
        List<Appointment> userAppointments = appointmentService.findAllByUser(user);

        model.addAttribute("user", user);
        /*model.addAttribute("servingList", userServings);*/
        model.addAttribute("appointmentList", userAppointments);

        return "include/profile";
    }

    @PostMapping("/profile/avatar")
    public String handleAvatarUpload(@RequestParam("avatar") MultipartFile avatar, Principal principal) throws IOException {
        if (avatar != null && !avatar.isEmpty()) {
            String relativePath = uploadPath;
            String realPath = servletContext.getRealPath(relativePath);
            if (realPath == null) {
                throw new IOException("Не удалось определить реальный путь для " + relativePath);
            }

            File uploadDir = new File(realPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new IOException("Не удалось создать директорию для загрузки файлов: " + realPath);
            }

            String filename = System.currentTimeMillis() + "_" + principal.getName() + ".jpg";
            File file = new File(uploadDir, filename);
            avatar.transferTo(file);

            Optional<User> optionalUser = userService.findByUsername(principal.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setAvatarUrl(relativePath + "/" + filename);
                userService.save(user);
            } else {
                throw new IOException("Пользователь не найден: " + principal.getName());
            }
        }
        return "redirect:/profile";
    }
}