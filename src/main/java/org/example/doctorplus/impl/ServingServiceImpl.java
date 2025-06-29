package org.example.doctorplus.impl;

import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.PatientRepo;
import org.example.doctorplus.repo.ServingRepo;
import org.example.doctorplus.repo.UserRepo;
import org.example.doctorplus.service.ServingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServingServiceImpl implements ServingService {

    private final ServingRepo servingRepo;
    private final UserRepo userRepo;
    private final PatientRepo patientRepo;

    public ServingServiceImpl(ServingRepo servingRepo, UserRepo userRepo, PatientRepo patientRepo) {
        this.servingRepo = servingRepo;
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public Serving save(Serving serving) {
        return servingRepo.save(serving);
    }

    @Override
    public Serving findById(Long id) {
        return servingRepo.findById(id).orElse(null);
    }

    @Override
    public List<Serving> findAll() {
        return servingRepo.findAll();
    }

    @Override
    public List<Serving> findByUsername(String username) {
        return servingRepo.findByUserUsername(username);
    }

    @Override
    public Serving update(Serving serving) {
        return servingRepo.save(serving);
    }

    @Override
    public void deleteById(Long id) {
        servingRepo.deleteById(id);
    }
}