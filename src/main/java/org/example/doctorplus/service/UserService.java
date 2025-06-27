package org.example.doctorplus.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void update(UserDTO dto);
    void deleteUser(Long id);
    User findById(Long id);
    List<User> findAll();
    List<User> findAllOrderByUsernameAsc();
    List<User> findAllOrderByUsernameDesc();
    List<User> findAllOrderByNameAsc();
    List<User> findAllOrderByNameDesc();
    void save(UserDTO dto);
    boolean isUsernameAvailable(String username);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(@NotBlank(message = "Логин не может быть пустым") @Size(min = 3, max = 20, message = "Логин должен содержать от 3 до 20 символов") String username);
    User save(User user);
}