package com.example.DoctorPlus.service;

import com.example.DoctorPlus.dto.UserDTO;
import com.example.DoctorPlus.model.Role;
import com.example.DoctorPlus.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    User save(UserDTO userDTO);
    boolean isUsernameAvailable(String username);
    User findById(Long id);
    void deleteUser(Long id);
    List<User> findAll();
    List<User> findAllOrderByUsernameAsc();
    List<User> findAllOrderByUsernameDesc();
    List<User> findAllOrderByNameAsc();
    List<User> findAllOrderByNameDesc();
    void updateUserRoles(Long userId, Set<Role> roles);
    Optional<User> findByUsername(String username);
    User save(User user);
    String saveAvatarFile(MultipartFile file);
}