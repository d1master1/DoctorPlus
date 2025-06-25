package org.example.doctorplus.repo;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    List<Patient> findAllByOrderBySurnameAsc();
    List<Patient> findAllByOrderBySurnameDesc();
    List<Patient> findAllByOrderByNameAsc();
    List<Patient> findAllByOrderByNameDesc();
    Optional<Patient> findByUser(User user);
    boolean existsById(Long id);
}