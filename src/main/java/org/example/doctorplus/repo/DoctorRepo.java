package org.example.doctorplus.repo;

import org.example.doctorplus.model.Doctor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    List<Doctor> findAll(Sort sort);
    @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.user")
    List<Doctor> findAllWithUser();
    List<Doctor> findAllByOrderByNameAsc();
    List<Doctor> findAllByOrderByNameDesc();
    @Query("SELECT d FROM Doctor d ORDER BY d.speciality ASC")
    List<Doctor> findAllBySpecialityAsc();
    @Query("SELECT d FROM Doctor d ORDER BY d.speciality DESC")
    List<Doctor> findAllBySpecialityDesc();
}