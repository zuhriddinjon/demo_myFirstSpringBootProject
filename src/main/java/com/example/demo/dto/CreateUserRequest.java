package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = "Ism bo‘sh bo‘lishi mumkin emas")
        String name,

        @NotBlank(message = "Email bo‘sh bo‘lishi mumkin emas")
        String email,

        @NotBlank(message = "birthDate bo‘sh bo‘lishi mumkin emas")
        LocalDate birthDate
) {
}
