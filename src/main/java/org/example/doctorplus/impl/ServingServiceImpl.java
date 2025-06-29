package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.dto.ServingDTO;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.repo.ServingRepo;
import org.example.doctorplus.service.ServingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServingServiceImpl implements ServingService {

    private final ServingRepo servingRepo;

    @Override
    public Serving save(Serving serving) {
        return servingRepo.save(serving);
    }

    @Override
    public void deleteById(Long id) {
        servingRepo.deleteById(id);
    }

    @Override
    public Optional<Serving> findById(Long id) {
        return servingRepo.findById(id);
    }

    @Override
    public List<ServingDTO> findByUsername(String username) {
        List<Serving> servings = servingRepo.findByUserUsername(username);
        return servings.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ServingDTO convertToDTO(Serving serving) {
        ServingDTO dto = new ServingDTO();
        dto.setId(serving.getId());
        dto.setName(serving.getName());
        dto.setDescription(serving.getDescription());
        dto.setCost(serving.getCost());
        dto.setUsername(serving.getUser().getUsername());
        return dto;
    }

    @Override
    public List<Serving> findAll() {
        return servingRepo.findAll();
    }
}