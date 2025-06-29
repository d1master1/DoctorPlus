package org.example.doctorplus.repo;

import jakarta.persistence.metamodel.SingularAttribute;
import org.example.doctorplus.model.Appointment;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {
    List<Patient> findAll(Sort sort);
    List<Appointment> findById(SingularAttribute<AbstractPersistable, Serializable> id);
}