package com.example.DoctorPlus.model;

public enum Role {
    USER("Пользователь"),
    EMPLOYEE("Сотрудник"),
    ADMIN("Администратор");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}