package org.example.doctorplus.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;
    private String passport;
    private String phone;
    private String email;
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (surname != null) fullName.append(surname).append(" ");
        if (name != null) fullName.append(name);
        return fullName.toString().trim();
    }
}