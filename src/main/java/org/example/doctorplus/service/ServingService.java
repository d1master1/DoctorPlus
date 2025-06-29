package org.example.doctorplus.service;

import org.example.doctorplus.dto.ServingDTO;
import org.example.doctorplus.model.Serving;

import java.util.List;
import java.util.Optional;

public interface ServingService {
    Serving save(Serving serving);
    Optional<Serving> findById(Long id);
    void deleteById(Long id);
    List<Serving> findAll();
    List<ServingDTO> findByUsername(String username);
}