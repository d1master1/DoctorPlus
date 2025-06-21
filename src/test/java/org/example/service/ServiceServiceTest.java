package org.example.service;

import org.example.dto.ServiceDTO;
import org.example.repo.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepo;

    @InjectMocks
    private ServiceService serviceService;

    private ServiceDTO dto;

    @BeforeEach
    void setUp() {
        dto = ServiceDTO.builder()
                .name("Консультация")
                .price(1000.0)
                .build();
    }

    @Test
    void testFindAllServices_returnsEmptyList() {
        when(serviceRepo.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0, serviceService.findAll().size());
    }

    @Test
    void testCreateService_success() {
        assertDoesNotThrow(() -> serviceService.create(dto));
    }
}