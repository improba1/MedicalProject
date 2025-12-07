package com.example.demo.mapper.doctor;

import com.example.demo.dto.request.doctor.AddDoctorRequest;
import com.example.demo.dto.request.doctor.UpdateDoctorRequest;
import com.example.demo.dto.response.DoctorResponse;
import com.example.demo.dto.response.ImageResponse;
import com.example.demo.enums.Role;
import com.example.demo.enums.Specialization;
import com.example.demo.model.Doctor;
import com.example.demo.model.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    public Doctor toEntity(AddDoctorRequest request) {
        return Doctor.builder()
                .nickname(request.getNickname())
                .password(request.getPassword())
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .qualification(request.getQualification())
                .rating(request.getRating())
                .role(Role.DOCTOR)
                .specialization(Specialization.valueOf(request.getSpecialization()))
                .address(request.getAddress())
                .birthDate(request.getBirthDate())
                .sex(request.getSex())
                .startDate(request.getStartDate())
                .isActive(true)
                .build();
    }

    public void updateEntity(Doctor doctor, UpdateDoctorRequest request) {
        if (request.getQualification() != null) doctor.setQualification(request.getQualification());
        if (request.getRating() != null) doctor.setRating(request.getRating());
        if (request.getAddress() != null) doctor.setAddress(request.getAddress());
    }

    public DoctorResponse toResponse(Doctor doctor) {
        ImageResponse imageResponse = null;
        Image image = doctor.getImage();
        if (image != null) {
            imageResponse = ImageResponse.builder()
                    .id(image.getId())
                    .fileName(image.getFileName())
                    .fileType(image.getFileType())
                    .downloadUrl(image.getDownloadUrl())
                    .doctorId(doctor.getId())
                    .build();
        }

        return DoctorResponse.builder()
                .id(doctor.getId())
                .nickname(doctor.getNickname())
                .firstname(doctor.getFirstname())
                .lastname(doctor.getLastname())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .address(doctor.getAddress())
                .birthDate(doctor.getBirthDate())
                .sex(doctor.getSex() != null ? doctor.getSex().name() : null)
                .specialization(doctor.getSpecialization() != null ? doctor.getSpecialization().name() : null)
                .qualification(doctor.getQualification())
                .startDate(doctor.getStartDate())
                .experienceYears(doctor.getExperienceYears())
                .rating(doctor.getRating())
                .image(imageResponse)
                .build();
    }

    public List<DoctorResponse> toResponseList(List<Doctor> doctors) {
        return doctors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}