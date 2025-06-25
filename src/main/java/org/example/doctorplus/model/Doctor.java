package org.example.doctorplus.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (surname != null) fullName.append(surname).append(" ");
        if (name != null) fullName.append(name).append(" ");
        if (patronymic != null) fullName.append(patronymic);
        return fullName.toString().trim();
    }

    private String speciality;
    private String schedule;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}