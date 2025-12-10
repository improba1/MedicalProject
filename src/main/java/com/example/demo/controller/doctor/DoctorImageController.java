package com.example.demo.controller.doctor;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.model.Doctor;
import com.example.demo.service.doctor.DoctorService;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("${api.prefix}/doctor/me/images")
@RequiredArgsConstructor
public class DoctorImageController {

    private final DoctorService doctorService;
    private final ImageMapper imageMapper;
    private final ImageService imageService;

    // 🔹 Завантажити картинку для залогованого лікаря
    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile file) {
        Doctor currentDoctor = doctorService.getCurrentDoctor();
        Doctor updated = doctorService.updateOwnProfileWithImage(currentDoctor, file);
        ImageResponse response = imageMapper.toResponse(updated.getImage());
        return ResponseEntity.ok(response);
    }

    // 🔹 Оновити картинку для залогованого лікаря
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ImageResponse> updateImage(@RequestParam MultipartFile file) {
        Doctor currentDoctor = doctorService.getCurrentDoctor();
        Doctor updated = doctorService.updateOwnProfileWithImage(currentDoctor, file);
        ImageResponse response = imageMapper.toResponse(updated.getImage());
        return ResponseEntity.ok(response);
    }

    // 🔹 Завантажити картинку залогованого лікаря
    @GetMapping("/download")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<byte[]> downloadOwnImage() {
        return imageService.downloadOwnImage();
    }
}