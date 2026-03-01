package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isBlocked;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private String profilePhoto;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(unique = true)
    private String driverLicensePhoto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(precision = 3, scale = 2)
    private BigDecimal ratingAverage = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "driver")
    private List<Trip> trips;

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "reviewedUser")
    private List<Feedback> receivedFeedbacks;

    @OneToMany(mappedBy = "reviewer")
    private List<Feedback> givenFeedbacks;

    @OneToMany(mappedBy = "driver")
    private List<Car> cars;
}