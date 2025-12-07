package com.example.demo.controller.image;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(imageService.saveOwnImage(file));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('doctor:update')")
    public ResponseEntity<ImageResponse> updateImage(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(imageService.updateOwnImage(file));
    }
}