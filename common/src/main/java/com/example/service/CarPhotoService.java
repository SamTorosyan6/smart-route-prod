package com.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CarPhotoService {

    void savePhotos(Integer carId, List<MultipartFile> photos, Integer mainPhotoIndex) throws IOException;
}