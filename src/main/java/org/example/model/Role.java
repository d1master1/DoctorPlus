package org.example.model;

import lombok.Getter;

@Getter
public enum Role {
    USER("Пользователь"),
    EMPLOYEE("Сотрудник"),
    ADMIN("Администратор");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

}