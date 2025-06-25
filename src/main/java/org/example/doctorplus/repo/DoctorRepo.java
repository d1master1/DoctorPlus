package org.example.doctorplus.repo;

import org.example.doctorplus.model.Doctor;
import org.example.doctorplus.model.User;
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
    @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.user")
    List<Doctor> findAllWithUser();
}