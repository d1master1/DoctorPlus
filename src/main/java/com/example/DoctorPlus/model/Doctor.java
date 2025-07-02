package com.example.DoctorPlus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private String patronymic;

    private String speciality;

    private String schedule;

    /*private String phone;*/

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public String getFirstName() {
        return name;
    }

    public String getLastName() {
        return surname;
    }

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (surname != null) fullName.append(surname).append(" ");
        if (name != null) fullName.append(name).append(" ");
        if (patronymic != null) fullName.append(patronymic);
        return fullName.toString().trim();
    }
}