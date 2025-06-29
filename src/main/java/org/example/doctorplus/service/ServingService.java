package org.example.doctorplus.service;

import org.example.doctorplus.dto.ServingDTO;
import org.example.doctorplus.model.Serving;

import java.util.List;

public interface ServingService {
    Serving save(Serving serving);
    Serving findById(Long id);
    List<Serving> findAll();
    List<Serving> findByUsername(String username);
    Serving update(Serving serving);
    void deleteById(Long id);
}