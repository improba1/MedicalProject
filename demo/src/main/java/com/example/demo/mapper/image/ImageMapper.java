package com.example.demo.mapper.image;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.model.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    public ImageResponse toResponse(Image image) {
        return ImageResponse.builder()
                .id(image.getId())
                .fileName(image.getFileName())
                .fileType(image.getFileType())
                .downloadUrl(image.getDownloadUrl())
                .doctorId(
                        image.getDoctor() != null
                                ? image.getDoctor().getId()
                                : null
                )
                .build();
    }
}