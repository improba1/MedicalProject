package com.example.demo.controller.public_;

import com.example.demo.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/images/public")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/download/{imageId}")
    public ResponseEntity<byte[]> download(@PathVariable UUID imageId) {
        return imageService.downloadImage(imageId);
    }
}