package org.example.doctorplus.model;

import lombok.Getter;

@Getter
public enum Role {
    USER("Пользователь"),
    DOCTOR("Доктор"),
    ADMIN("Администратор");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}