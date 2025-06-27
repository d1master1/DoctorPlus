package org.example.doctorplus.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;
    private String speciality;
    private String schedule;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}