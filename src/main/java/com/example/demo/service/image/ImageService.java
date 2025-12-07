package com.example.demo.service.image;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageService {

    // 🔹 Методи для залогованого лікаря
    void deleteOwnImage();
    ImageResponse saveOwnImage(MultipartFile file);
    ImageResponse updateOwnImage(MultipartFile file);
    ResponseEntity<byte[]> downloadOwnImage() throws Exception;

    // 🔹 Методи для адміністратора
    Image getImageById(UUID id);
    void deleteImageById(UUID id);
    ImageResponse saveImage(UUID doctorId, MultipartFile file);
    ImageResponse updateImage(UUID imageId, MultipartFile file);
    ResponseEntity<byte[]> downloadImage(UUID imageId) throws Exception;
}