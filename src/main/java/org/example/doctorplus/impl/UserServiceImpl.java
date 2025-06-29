package org.example.doctorplus.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.UserRepo;
import org.example.doctorplus.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void update(UserDTO dto) {
        // 1. Найти пользователя по логину
        User user = userRepo.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        // 2. Обновить персональные данные
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());

        // 3. Обновить пароль, если он указан и подтверждён
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if (dto.isPasswordConfirmed()) {
                throw new IllegalArgumentException("Пароли не совпадают");
            }
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 4. Сохранить изменения
        userRepo.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findByUsername(id.toString()).orElse(null); // или правильная реализация по ID
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
    public boolean isUsernameAvailable(String username) {
        return !userRepo.existsByUsername(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void save(UserDTO dto) {
        if (!dto.isPasswordConfirmed()) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }

        if (userRepo.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Логин уже занят");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(Role.USER));

        userRepo.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepo.existsByUsername(username);
    }
}