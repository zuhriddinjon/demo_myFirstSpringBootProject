package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "Ism bo‘sh bo‘lishi mumkin emas")
        String name,

        @NotBlank(message = "Email bo‘sh bo‘lishi mumkin emas")
        String email
) {
}
