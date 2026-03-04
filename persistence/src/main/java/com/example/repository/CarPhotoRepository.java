package com.example.repository;

import com.example.model.CarPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarPhotoRepository extends JpaRepository<CarPhoto, Integer> {

    List<CarPhoto> findByCarId(Integer carId);
}