package org.example.doctorplus.impl;

import lombok.RequiredArgsConstructor;
import org.example.doctorplus.model.Patient;
import org.example.doctorplus.model.Serving;
import org.example.doctorplus.model.User;
import org.example.doctorplus.repo.ServingRepo;
import org.example.doctorplus.service.AppointmentService;
import org.example.doctorplus.service.ServingService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServingServiceImpl implements ServingService {

    private final ServingRepo servingRepo;
    private final AppointmentService appointmentService;

    @Override
    public List<Serving> findAll(Sort sort) {
        return servingRepo.findAll(sort);
    }

    @Override
    public Optional<Serving> findById(Long id) {
        return servingRepo.findById(id);
    }

    @Override
    public Serving save(Serving serving) {
        return servingRepo.save(serving);
    }

    @Override
    public Serving getServingById(Long id) {
        return servingRepo.findById(id).orElse(null);
    }

    @Override
    public void saveServing(Serving serving) {
        servingRepo.save(serving);
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
    public List<Serving> findAllByUser(User user) {
        return servingRepo.findByUser(user);
    }

    @Override
    public List<Serving> findAllSorted(Sort sort) {
        return servingRepo.findAll(sort);
    }

    @Override
    public List<Serving> deleteAllExceptWithAppointments() {
        List<Serving> allServings = servingRepo.findAll();
        List<Serving> notDeleted = new ArrayList<>();

        for (Serving serving : allServings) {
            if (appointmentService.existsByServing(serving)) {
                notDeleted.add(serving);
            } else {
                servingRepo.delete(serving);
            }
        }

        return notDeleted;
    }

    @Override
    public boolean deleteByIdIfPossible(Long id) {
        Serving serving = servingRepo.findById(id).orElse(null);

        if (serving == null) {
            return false;
        }

        // Если услуга используется в приёме — не удаляем
        if (appointmentService.existsByServing(serving)) {
            return false;
        }

        servingRepo.deleteById(id);
        return true;
    }

    @Override
    public List<Serving> findAllSorted(String sortField, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (sortField == null || sortField.isBlank()) {
            sortField = "name";
            direction = Sort.Direction.ASC;
        }

        return servingRepo.findAll(Sort.by(direction, sortField));
    }

    @Override
    public List<Serving> findAll() {
        return servingRepo.findAll();
    }
}