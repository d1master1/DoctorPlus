package org.example.service;

import org.example.dto.ServiceDTO;
import org.example.model.Service;
import org.example.repo.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepo;

    public List<ServiceDTO> findAllSorted(String sort) {
        List<Service> services = switch (sort) {
            case "name" -> serviceRepo.findAllByOrderByNameAsc();
            case "price" -> serviceRepo.findAllByOrderByPriceAsc();
            default -> serviceRepo.findAll();
        };
        return services.stream()
                .map(s -> new ServiceDTO())
                .collect(Collectors.toList());
    }

    public void create(ServiceDTO dto) {
        Service service = new Service();
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        serviceRepo.save(service);
    }

    public void update(ServiceDTO dto) {
        Service service = serviceRepo.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        serviceRepo.save(service);
    }

    public void delete(Long id) {
        serviceRepo.deleteById(id);
    }

    public ServiceDTO findById(Long id) {
        return serviceRepo.findById(id)
                .map(s -> new ServiceDTO())
                .orElseThrow(() -> new IllegalArgumentException("Услуга не найдена"));
    }

    public List<ServiceDTO> findAll() {
        return serviceRepo.findAll().stream()
                .map(s -> new ServiceDTO())
                .collect(Collectors.toList());
    }
}