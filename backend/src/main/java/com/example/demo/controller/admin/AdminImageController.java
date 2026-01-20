package com.example.demo.controller.admin;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.ImageResponse;
import com.example.demo.mapper.ImageMapper;
import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<ImageResponse>> getImageById(
            @PathVariable UUID imageId
    ) {
        var image = imageService.getImageById(imageId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Image retrieved successfully",
                        imageMapper.toResponse(image)
                ));
    }

    @PostMapping("/upload/{doctorId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(
            @PathVariable UUID doctorId,
            @RequestParam MultipartFile file
    ) {
        var saved = imageService.saveImage(doctorId, file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(
                        HttpStatus.CREATED.value(),
                        "Image uploaded successfully",
                        imageMapper.toResponse(saved)
                ));
    }

    @PutMapping("/update/{imageId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<ImageResponse>> updateImage(
            @PathVariable UUID imageId,
            @RequestParam MultipartFile file
    ) {
        var updated = imageService.updateImage(imageId, file);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Image updated successfully",
                        imageMapper.toResponse(updated)
                ));
    }

    @DeleteMapping("/delete/{imageId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<Void>> deleteImage(
            @PathVariable UUID imageId
    ) {
        imageService.deleteImageById(imageId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(
                        HttpStatus.OK.value(),
                        "Image deleted successfully",
                        null
                ));
    }
}