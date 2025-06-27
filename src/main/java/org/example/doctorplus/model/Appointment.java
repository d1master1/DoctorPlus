package org.example.doctorplus.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "serving_id")
    private Serving serving;

    // Форматированная дата
    public String getFormattedDate() {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : "-";
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}