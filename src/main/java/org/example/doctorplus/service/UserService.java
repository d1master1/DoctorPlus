package org.example.doctorplus.service;

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
}