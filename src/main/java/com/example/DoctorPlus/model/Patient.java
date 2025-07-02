package com.example.DoctorPlus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;

    // Вспомогательный метод для отображения полного имени клиента
    public String getFullName() {
        return surname + " " + name + (patronymic != null ? " " + patronymic : "");
    }
    
    private String birth;
    private String phone;
    /*private String ownerType;*/
    private String email;
    private String address;

    // Связь OneToOne с User — владелец клиента
    @OneToOne(fetch = FetchType.LAZY, optional = true) // Устанавливаем optional = true
    @JoinColumn(name = "user_id", unique = true, nullable = true) // Устанавливаем nullable = true
    private User user;
}