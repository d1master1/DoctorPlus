package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.repo.AppointmentRepo;
import org.example.doctorplus.repo.ServingRepo;
import org.example.doctorplus.service.ServingService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServingServiceImpl implements ServingService {

    private final ServingRepo servingRepo;
    private final AppointmentRepo appointmentRepo;

    @Override
    public List<Serving> findAll() {
        return servingRepo.findAll();
    }

    @Override
    public Serving getServingById(Long id) {
        return servingRepo.findById(id).orElse(null);
    }

    @Override
    public Serving saveServing(Serving serving) {
        return servingRepo.save(serving);
    }

    @Override
    public void deleteServing(Long id) {
        servingRepo.deleteById(id);
    }

    @Override
    public void deleteAll() {
        servingRepo.deleteAll();
    }

    @Override
    public List<Serving> findByPatient(Patient patient) {
        return servingRepo.findByPatient(patient);
    }

    @Override
    public List<Serving> deleteAllExceptWithAppointments() {
        List<Serving> allServings = servingRepo.findAll();
        List<Serving> undeleted = new ArrayList<>();
        for (Serving serving : allServings) {
            if (appointmentRepo.existsByServing(serving)) {
                undeleted.add(serving);
            } else {
                servingRepo.delete(serving);
            }
        }
        return undeleted;
    }
}