package org.example.doctorplus.repo;

import org.example.doctorplus.model.Serving;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServingRepo extends JpaRepository<Serving, Long> {
    List<Serving> findByUserUsername(String username);
}