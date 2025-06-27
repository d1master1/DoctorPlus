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

    private String name;
    private String description;
    private BigDecimal cost;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    // Пациент (опционально)
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}