package com.example.demo.mapper;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageMapper {

    public ImageResponse toResponse(Image image) {
        if (image == null) {
            return null;
        }

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

    public List<ImageResponse> toResponseList(List<Image> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}