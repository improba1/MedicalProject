package com.example.demo.service.image;

import com.example.demo.dto.response.ImageResponse;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.image.ImageMapper;
import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.doctor.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final DoctorService doctorService;
    private final ImageMapper imageMapper;

    @Override
    public Image getImageById(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteOwnImage() {
        var doctor = doctorService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to delete");
        }
        imageRepository.delete(image);
        doctor.setImage(null);
        doctorService.update(doctor);
    }

    @Override
    public ImageResponse saveOwnImage(MultipartFile file) {
        var doctor = doctorService.getAuthenticatedDoctor();

        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setDoctor(doctor);

            Image saved = imageRepository.save(image);

            // формуємо URL
            String downloadUrl = "/api/v1/images/download/" + saved.getId();
            saved.setDownloadUrl(downloadUrl);
            saved = imageRepository.save(saved);

            // Прив’язуємо фото до лікаря
            doctor.setImage(saved);
            doctorService.update(doctor);

            return imageMapper.toResponse(saved);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageResponse updateOwnImage(MultipartFile file) {
        var doctor = doctorService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to update");
        }

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            Image updated = imageRepository.save(image);
            return imageMapper.toResponse(updated);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadOwnImage() throws Exception {
        var doctor = doctorService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to download");
        }

        byte[] bytes = image.getImage().getBytes(1, (int) image.getImage().length());

        return ResponseEntity.ok()
                .header("Content-Type", image.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + image.getFileName() + "\"")
                .body(bytes);
    }

    @Override
    public void deleteImageById(UUID id) {
        Image image = getImageById(id);
        imageRepository.delete(image);
    }

    @Override
    public ImageResponse saveImage(UUID doctorId, MultipartFile file) {
        var doctor = doctorService.getById(doctorId);

        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            image.setDoctor(doctor);

            Image saved = imageRepository.save(image);

            String downloadUrl = "/api/v1/admin/images/download/" + saved.getId();
            saved.setDownloadUrl(downloadUrl);
            saved = imageRepository.save(saved);

            doctor.setImage(saved);
            doctorService.update(doctor);

            return imageMapper.toResponse(saved);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageResponse updateImage(UUID imageId, MultipartFile file) {
        Image image = getImageById(imageId);

        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            Image updated = imageRepository.save(image);
            return imageMapper.toResponse(updated);

        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadImage(UUID imageId) throws Exception {
        Image image = getImageById(imageId);

        byte[] bytes = image.getImage().getBytes(1, (int) image.getImage().length());

        return ResponseEntity.ok()
                .header("Content-Type", image.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + image.getFileName() + "\"")
                .body(bytes);
    }

}