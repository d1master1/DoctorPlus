package org.example.doctorplus.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void update(UserDTO dto);
    List<User> findAll();
    void save(UserDTO dto);
    Optional<User> findByUsername(String username);
    User save(User user);
    Optional<User> findById(Long id);
}