package com.example.demo.dto.request.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class ImageCreateRequest {
    private UUID doctorId;
    private MultipartFile file;
}