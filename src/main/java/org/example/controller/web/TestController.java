package org.example.controller.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class TestController {

    private final DataSource dataSource;

    public TestController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/test")
    public String testConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return "✅ Подключение установлено";
        } catch (Exception e) {
            return "❌ Ошибка подключения: " + e.getMessage();
        }
    }
}