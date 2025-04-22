package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    @Column(name = "email", length = 100, unique = true)
    private String email;
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dataOfBirth;
    @Column(name = "country", nullable = false, length = 50)
    private String country;
}
