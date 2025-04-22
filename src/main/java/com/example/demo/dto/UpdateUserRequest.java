package com.example.demo.dto;

import java.time.LocalDate;

public record UpdateUserRequest(
        String name,
        String email,
        LocalDate birthDate
) {
}
