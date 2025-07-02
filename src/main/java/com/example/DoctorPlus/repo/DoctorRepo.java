package com.example.DoctorPlus.repo;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    List<Doctor> findAll(Sort sort);
    Doctor findByUserId(Long userId);
    @Query("SELECT e FROM Doctor e LEFT JOIN FETCH e.user")
    List<Doctor> findAllWithUser();
    boolean existsById(Long id);
}