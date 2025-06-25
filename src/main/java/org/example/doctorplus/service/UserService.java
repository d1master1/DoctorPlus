package org.example.doctorplus.service;

import org.example.doctorplus.dto.UserDTO;
import org.example.doctorplus.model.Role;
import org.example.doctorplus.model.User;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    void deleteUser(Long id);
    List<User> findAll();
    List<User> findAllOrderByUsernameAsc();
    List<User> findAllOrderByUsernameDesc();
    List<User> findAllOrderByNameAsc();
    List<User> findAllOrderByNameDesc();
    void updateUserRoles(Long userId, Set<Role> roles);
    Optional<User> findByUsername(String username);
    void save(User user);
    String saveAvatarFile(MultipartFile file);
    void save(UserDTO dto);
    void update(UserDTO dto);
    User findById(Long id);
    boolean isUsernameAvailable(String username);
}