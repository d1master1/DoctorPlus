package com.example.DoctorPlus.repo;

import com.example.DoctorPlus.model.Patient;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServingRepo extends JpaRepository<Serving, Long> {
    List<Serving> findByPatient(Patient patient);
    /*List<Serving> findAllByOwner(User owner);*/
    @Query("SELECT s FROM Serving s WHERE s.patient.user = :user")
    List<Serving> findAllByPatientUser(@Param("user") User user);
    // Удалить все объекты, ID которых не входят в переданный список
    @Modifying
    @Transactional
    @Query("DELETE FROM Serving r WHERE r.id NOT IN :ids")
    int deleteByIdNotIn(@Param("ids") List<Long> ids);
}