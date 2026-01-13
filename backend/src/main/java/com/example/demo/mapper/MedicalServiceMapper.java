package com.example.demo.mapper;

import com.example.demo.dto.request.medical_service.MedicalServiceCreateRequest;
import com.example.demo.dto.request.medical_service.MedicalServiceUpdateRequest;
import com.example.demo.dto.response.MedicalServiceResponse;
import com.example.demo.model.MedicalService;
import org.springframework.stereotype.Component;

@Component
public class MedicalServiceMapper {

    public MedicalService fromCreateRequest(MedicalServiceCreateRequest req) {
        return MedicalService.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .active(true)
                .build();
    }

    public void toUpdateEntity(MedicalService service, MedicalServiceUpdateRequest req) {
        if (req.getName() != null) service.setName(req.getName());
        if (req.getDescription() != null) service.setDescription(req.getDescription());
        if (req.getPrice() != null) service.setPrice(req.getPrice());
        if (req.getActive() != null) service.setActive(req.getActive());
    }

    public MedicalServiceResponse toResponse(MedicalService service) {
        return MedicalServiceResponse.builder()
                .id(service.getId())
                .doctorId(service.getDoctor().getId())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .active(service.isActive())
                .build();
    }
}