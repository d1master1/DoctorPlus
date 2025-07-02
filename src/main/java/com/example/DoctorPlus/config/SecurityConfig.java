package com.example.DoctorPlus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/login", "/include/registration/**", "/logout",
                                "/webjars/**", "/css/**", "/js/**", "/images/**", "/fonts/**",
                                "/header", "/footer", "/about", "/include/about",
                                "/uploads/**", "/img/**"
                        ).permitAll()
                        .requestMatchers("/", "/serving/serving_list", "/include/profile", "/include/profile/**", "/appointment/appointment_list")
                        .hasAnyRole("USER", "EMPLOYEE", "ADMIN")
                        .requestMatchers("/serving/serving_form", "/appointment/appointment_form", "/patient/patient_list", "/appointment/appointment_form")
                        .hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/doctor/doctor_form", "/doctor/doctor_list", "/user/user_list", "/user/user_form")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/profile", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}