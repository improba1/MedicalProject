package com.example.demo.service.image;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Doctor;
import com.example.demo.model.Image;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.service.auth.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final DoctorRepository doctorRepository;
    private final CurrentUserService currentUserService;

    @Override
    public Image getImageById(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public void deleteOwnImage() {
        var doctor = currentUserService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to delete");
        }
        imageRepository.delete(image);
        doctor.setImage(null);
        doctorRepository.save(doctor);
    }

    @Override
    public Image saveOwnImage(MultipartFile file) {
        var doctor = currentUserService.getAuthenticatedDoctor();
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageData(file.getBytes());
            image.setDoctor(doctor);

            Image saved = imageRepository.save(image);
            saved.setDownloadUrl("/api/v1/images/public/download/" + saved.getId());
            saved = imageRepository.save(saved);

            doctor.setImage(saved);
            doctorRepository.save(doctor);

            return saved;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage(), e);
        }
    }

    @Override
    public Image updateOwnImage(MultipartFile file) {
        var doctor = currentUserService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to update");
        }
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageData(file.getBytes());
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadOwnImage() {
        var doctor = currentUserService.getAuthenticatedDoctor();
        Image image = doctor.getImage();
        if (image == null) {
            throw new ResourceNotFoundException("Doctor has no image to download");
        }
        return ResponseEntity.ok()
                .header("Content-Type", image.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + image.getFileName() + "\"")
                .body(image.getImageData());
    }

    @Override
    public void deleteImageById(UUID id) {
        Image image = getImageById(id);

        Doctor doctor = image.getDoctor();
        if (doctor != null) {
            doctor.setImage(null);
            doctorRepository.save(doctor);
        }

        imageRepository.delete(image);
    }


    @Override
    public Image saveImage(UUID doctorId, MultipartFile file) {
        var doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageData(file.getBytes());
            image.setDoctor(doctor);

            Image saved = imageRepository.save(image);
            saved.setDownloadUrl("/api/v1/images/public/download/" + saved.getId());
            saved = imageRepository.save(saved);

            doctor.setImage(saved);
            doctorRepository.save(doctor);

            return saved;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image: " + e.getMessage(), e);
        }
    }

    @Override
    public Image updateImage(UUID imageId, MultipartFile file) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImageData(file.getBytes());
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage(), e);
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadImage(UUID imageId) {
        Image image = getImageById(imageId);
        return ResponseEntity.ok()
                .header("Content-Type", image.getFileType())
                .header("Content-Disposition", "attachment; filename=\"" + image.getFileName() + "\"")
                .body(image.getImageData());
    }
}