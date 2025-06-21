package org.example.repo;

import org.example.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAll();
    List<Service> findAllByOrderByNameAsc();
    List<Service> findAllByOrderByPriceAsc();
}