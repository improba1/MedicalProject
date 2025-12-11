package com.example.demo.service.image;

import com.example.demo.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {

    Image getImageById(UUID id);

    void deleteOwnImage();

    Image saveOwnImage(MultipartFile file);

    Image updateOwnImage(MultipartFile file);

    ResponseEntity<byte[]> downloadOwnImage();

    void deleteImageById(UUID id);

    Image saveImage(UUID doctorId, MultipartFile file);

    Image updateImage(UUID imageId, MultipartFile file);

    ResponseEntity<byte[]> downloadImage(UUID imageId);
}