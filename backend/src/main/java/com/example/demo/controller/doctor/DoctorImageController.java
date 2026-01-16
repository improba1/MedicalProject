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

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam MultipartFile file
    ) {
        Doctor updated = doctorService.updateCurrentDoctorImage(file);
        return ResponseEntity.ok(
                imageMapper.toResponse(updated.getImage())
        );
    }

    @GetMapping("/download")
    @PreAuthorize("hasAuthority('doctor:read')")
    public ResponseEntity<byte[]> downloadOwnImage() {
        return imageService.downloadOwnImage();
    }
}