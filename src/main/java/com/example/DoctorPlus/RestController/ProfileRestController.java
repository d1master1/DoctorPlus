package com.example.DoctorPlus.RestController;

import com.example.DoctorPlus.model.Appointment;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.service.AppointmentService;
import com.example.DoctorPlus.service.ServingService;
import com.example.DoctorPlus.service.UserService;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileRestController {

    private final UserService userService;
    private final ServingService servingService;
    private final AppointmentService appointmentService;
    private final ServletContext servletContext;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    // Получить данные текущего пользователя
    @GetMapping
    public ResponseEntity<User> getCurrentUserProfile(Principal principal) {
        Optional<User> userOpt = userService.findByUsername(principal.getName());
        return userOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    // Получить все записи текущего пользователя
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getUserAppointments(Principal principal) {
        Optional<User> userOpt = userService.findByUsername(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        List<Appointment> appointments = appointmentService.findAllByUser(userOpt.get());
        return ResponseEntity.ok(appointments);
    }

    // Загрузить аватар
    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Principal principal) throws IOException {
        if (avatar == null || avatar.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл не выбран");
        }

        String realPath = servletContext.getRealPath(uploadPath);
        if (realPath == null) {
            return ResponseEntity.status(500).body("Не удалось определить путь для загрузки");
        }

        File uploadDir = new File(realPath);
        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
            return ResponseEntity.status(500).body("Не удалось создать директорию");
        }

        String filename = System.currentTimeMillis() + "_" + principal.getName() + ".jpg";
        File file = new File(uploadDir, filename);
        avatar.transferTo(file);

        Optional<User> optionalUser = userService.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAvatarUrl(uploadPath + "/" + filename);
            userService.save(user);
            return ResponseEntity.ok("Аватар успешно загружен");
        } else {
            return ResponseEntity.status(404).body("Пользователь не найден");
        }
    }

    // Получить аватар по имени файла
    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename) throws MalformedURLException {
        String realPath = servletContext.getRealPath(uploadPath);
        File file = new File(realPath, filename);
        Resource resource = new UrlResource(file.toURI());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .body(resource);
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}