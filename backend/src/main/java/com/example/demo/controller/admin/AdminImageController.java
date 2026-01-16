package com.example.demo.controller.admin;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.ImageResponse;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/admin/images")
@RequiredArgsConstructor
public class AdminImageController {

    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @GetMapping("/get/{imageId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<ImageResponse>> getImageById(@PathVariable UUID imageId) {
        var image = imageService.getImageById(imageId);
        return ResponseEntity.ok(
                ApiResponse.of(
                        200,
                        "Image retrieved successfully",
                        imageMapper.toResponse(image)
                )
        );
    }

    @PostMapping("/upload/{doctorId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ImageResponse> uploadImage(
            @PathVariable UUID doctorId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(imageMapper.toResponse(imageService.saveImage(doctorId, file)));
    }

    @PutMapping("/update/{imageId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ImageResponse> updateImage(
            @PathVariable UUID imageId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(imageMapper.toResponse(imageService.updateImage(imageId, file)));
    }

    @DeleteMapping("/delete/{imageId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID imageId) {
        imageService.deleteImageById(imageId);
        return ResponseEntity.noContent().build();
    }
}