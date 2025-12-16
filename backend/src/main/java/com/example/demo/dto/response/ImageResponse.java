package com.example.demo.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ImageResponse {
    private UUID id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
    private UUID doctorId;
}