package com.example.service.impl;

import com.example.model.Car;
import com.example.model.CarPhoto;
import com.example.repository.CarPhotoRepository;
import com.example.repository.CarRepository;
import com.example.service.CarPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarPhotoServiceImpl implements CarPhotoService {

    private final CarPhotoRepository carPhotoRepository;
    private final CarRepository carRepository;

    @Value("${app.upload.path}")
    private String uploadPath;

    @Override
    public void savePhotos(Integer carId, List<MultipartFile> photos, Integer mainPhotoIndex) throws IOException {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Car not found!"));

        Path dir = Paths.get(uploadPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        for (int i = 0; i < photos.size(); i++) {
            MultipartFile file = photos.get(i);
            if (file.isEmpty()) continue;

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), dir.resolve(filename));

            CarPhoto carPhoto = new CarPhoto();
            carPhoto.setCar(car);
            carPhoto.setPhotoUrl(uploadPath + filename);
            carPhoto.setMain(i == mainPhotoIndex);
            carPhotoRepository.save(carPhoto);
        }
    }
}