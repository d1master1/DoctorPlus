package org.example.doctorplus.impl;

import lombok.*;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.UserRepo;
import org.example.doctorplus.service.PatientService;
import org.example.doctorplus.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final PatientService patientService;

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<User> findAllOrderByUsernameAsc() {
        return userRepo.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }

    @Override
    public List<User> findAllOrderByUsernameDesc() {
        return userRepo.findAll(Sort.by(Sort.Direction.DESC, "username"));
    }

    @Override
    public List<User> findAllOrderByNameAsc() {
        return userRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<User> findAllOrderByNameDesc() {
        return userRepo.findAll(Sort.by(Sort.Direction.DESC, "name"));
    }

    @Override
    public void updateUserRoles(Long userId, Set<Role> roles) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRoles(roles);
        userRepo.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public String saveAvatar(User user, MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Можно загружать только изображения.");
            }

            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path uploadPath = Paths.get("src/main/resources/static/uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

    @Override
    public void save(User user) {
        userRepo.save(user);
    }

    @Override
    public String saveAvatarFile(MultipartFile file) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Можно загружать только изображения.");
            }

            String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path uploadPath = Paths.get("src/main/resources/static/uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

    @Override
    public void save(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl("/img/default-avatar.png");

        user.setRoles(new HashSet<>(dto.getRoles()));

        userRepo.save(user);

        // Сохраняем пациента только если он ещё не существует
        Patient patient = patientService.findByUser(user).orElse(new Patient());
        patient.setId(user.getId());
        patient.setName(dto.getName());
        patient.setSurname(dto.getSurname());
        patient.setPassport(dto.getPassport()); // может быть null
        patient.setAddress(dto.getAddress());   // может быть null
        patient.setBirthDate(dto.getBirthDate()); // может быть null
        patient.setUser(user);

        patientService.save(patient);
    }

    @Override
    public void update(UserDTO dto) {
        Optional<User> optionalUser = userRepo.findById(dto.getId());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        User user = optionalUser.get();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRoles(new HashSet<>(dto.getRoles()));

        userRepo.save(user);

        Patient patient = patientService.findByUser(user).orElse(new Patient());
        patient.setName(dto.getName());
        patient.setSurname(dto.getSurname());
        patient.setPassport(dto.getPassport());
        patient.setAddress(dto.getAddress());
        patient.setUser(user);

        patientService.save(patient);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepo.existsByUsername(username);
    }
}