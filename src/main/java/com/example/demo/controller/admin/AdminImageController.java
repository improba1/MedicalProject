package com.example.demo.controller.admin;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/images")
@RequiredArgsConstructor
public class AdminImageController {

    private final ImageService imageService;

    // 🔹 Завантажити фото лікаря за його ID
    @PostMapping("/upload/{doctorId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable UUID doctorId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(imageService.saveImage(doctorId, file));
    }

    // 🔹 Оновити фото за ID картинки
    @PutMapping("/update/{imageId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ImageResponse> updateImage(
            @PathVariable UUID imageId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(imageService.updateImage(imageId, file));
    }

    // 🔹 Завантажити фото за ID картинки
    @GetMapping("/download/{imageId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<byte[]> download(@PathVariable UUID imageId) throws Exception {
        return imageService.downloadImage(imageId);
    }

    // 🔹 Видалити фото за ID картинки
    @DeleteMapping("/delete/{imageId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        imageService.deleteImageById(imageId);
        return ResponseEntity.noContent().build();
    }
}