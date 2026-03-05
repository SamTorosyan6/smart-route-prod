package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "is_blocked",nullable = false)
    private boolean isBlocked;

    @CreationTimestamp
    @Column(name = "created_at",   nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "date_of_birth",nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "driver_license_photo",unique = true)
    private String driverLicensePhoto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "rating_average",precision = 4, scale = 2)
    private BigDecimal ratingAverage = BigDecimal.ZERO;

    @ToString.Exclude
    @ManyToOne
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