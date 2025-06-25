package org.example.doctorplus.repo;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServingRepo extends JpaRepository<Serving, Long> {
    List<Serving> findByPatient(Patient patient);
}