package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ImageService {

    public ResponseEntity<byte[]> getImage(String uploadPath, String filName){
        File file = new File(uploadPath + filName);

        if(!file.exists()){
            return ResponseEntity.notFound().build();
        }
        try {
            byte[] imageBytes = FileUtils.readFileToByteArray(file);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file.toPath())).body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
