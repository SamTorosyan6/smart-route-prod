package com.example.repository;

import com.example.model.entitiy.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Optional<Car> findByDriverId(Integer driverId);
}