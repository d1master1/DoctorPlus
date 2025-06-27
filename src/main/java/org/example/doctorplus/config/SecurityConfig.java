package org.example.doctorplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        // Доступ для всех (гости)
                        .requestMatchers(
                                "/", "/resources/**", "/register", "/login", "/include/registration/**", "/logout", "/webjars/**", "/css/**", "/img/**",
                                "/js/**", "/images/**", "/fonts/**", "/header", "/footer", "/about", "/profile", "/profile/**", "/include/about", "/uploads/**"
                        ).permitAll()

                        // Только для авторизированных пользователей
                        .requestMatchers( "/appointment/appointment_list", "/serving/serving_list")
                        .authenticated()

                        // Только доктора и админы
                        .requestMatchers("/serving/serving_form", "/appointment/appointment_form", "/patient/patient_list", "/patient/patient_form")
                        .hasAnyRole("DOCTOR", "ADMIN")

                        // Только администраторы
                        .requestMatchers("/doctor/doctor_form", "/doctor/doctor_list", "/user/user_list", "/user/user_form")
                        .hasRole("ADMIN")

                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}