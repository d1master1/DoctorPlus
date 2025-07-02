package com.example.DoctorPlus.impl;

import com.example.DoctorPlus.model.Doctor;
import com.example.DoctorPlus.model.Serving;
import com.example.DoctorPlus.model.User;
import com.example.DoctorPlus.repo.AppointmentRepo;
import com.example.DoctorPlus.repo.DoctorRepo;
import com.example.DoctorPlus.repo.ServingRepo;
import com.example.DoctorPlus.service.ServingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServingServiceImpl implements ServingService {

    private final ServingRepo servingRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;

    @Override
    public boolean existsByServingId(Long id) {
        return servingRepo.existsById(id);
    }

    @Override
    public Serving getServingById(Long id) {
        return servingRepo.findById(id).orElse(null);
    }

    @Override
    public Serving saveServing(Serving serving) {
        servingRepo.save(serving);
        return serving;
    }

    @Override
    public void deleteServing(Long id) {
        servingRepo.deleteById(id);
    }

    @Override
    public List<Serving> findAll() {
        return servingRepo.findAll();
    }

    @Override
    public void deleteAll() {
        servingRepo.deleteAll();
    }

    @Override
    public Doctor findByUser(User user) {
        return doctorRepo.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Доктор не найден"));
    }

    @Override
    public void deleteById(Long id) {
        servingRepo.deleteById(id);
    }

    @Override
    public List<Serving> findAllByPatientUser(User user) {
        return servingRepo.findAllByPatientUser(user);
    }

    @Override
    public int deleteAllExceptInAppointments() {
        List<Long> servingIdsInAppointments = appointmentRepo.findAll()
                .stream()
                .map(deal -> deal.getServing().getId())
                .distinct()
                .toList();

        if (servingIdsInAppointments.isEmpty()) {
            int count = (int) servingRepo.count();
            servingRepo.deleteAll();
            return count;
        }

        return servingRepo.deleteByIdNotIn(servingIdsInAppointments);
    }

    @Override
    public int deleteUnlinkedServings() {
        List<Serving> all = servingRepo.findAll();
        int deleted = 0;
        for (Serving serving : all) {
            if (appointmentRepo.findByServing(serving).isEmpty()) {
                servingRepo.delete(serving);
                deleted++;
            }
        }
        return deleted;
    }

    @Override
    public int countLinkedServings() {
        return (int) servingRepo.findAll().stream()
                .filter(serving -> !appointmentRepo.findByServing(serving).isEmpty()).count();
    }

    @Override
    public List<Serving> deleteAllExceptWithAppointments() {
        List<Serving> all = servingRepo.findAll();
        List<Serving> toDelete = all.stream()
                .filter(serving -> appointmentRepo.findByServing(serving).isEmpty())
                .collect(Collectors.toList());
        servingRepo.deleteAll(toDelete);
        return toDelete;
    }
}