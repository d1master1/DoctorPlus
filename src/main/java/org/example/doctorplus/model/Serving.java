package org.example.doctorplus.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "serving")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Serving {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название услуги
    @Column(nullable = false)
    private String name;

    // Описание услуги
    @Column(columnDefinition = "TEXT")
    private String description;

    // Стоимость
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cost;

    // Дата создания
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    // Пациент (опционально)
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    // Пользователь (врач или админ, назначивший услугу)
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}